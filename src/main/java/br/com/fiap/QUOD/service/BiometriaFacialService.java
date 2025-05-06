package br.com.fiap.QUOD.service;
import br.com.fiap.QUOD.dto.NotificacaoFraudeRequest;
import br.com.fiap.QUOD.model.BiometriaFacial;
import br.com.fiap.QUOD.model.NotificacaoFraude;
import br.com.fiap.QUOD.repository.BiometriaFacialRepository;
import br.com.fiap.QUOD.util.NotificacaoFraudeUtil;
import org.bson.types.Binary;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

@Service
public class BiometriaFacialService {

    @Autowired
    private BiometriaFacialRepository biometriaFacialRepository;
    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList("image/jpeg", "image/png");
    private static final long TAMANHO_MAX = 5 * 1024 * 1024; // 5MB
    private static final int LARGURA_MIN = 200;
    private static final int ALTURA_MIN = 200;
    private static final CascadeClassifier FACE_DETECTOR;

    static {
        nu.pattern.OpenCV.loadLocally();
    }

    static {
        try (InputStream is = BiometriaFacialService.class.getClassLoader()
                .getResourceAsStream("haarcascade_frontalface_default.xml")) {

            if (is == null) {
                throw new IOException("Arquivo haarcascade_frontalface_default.xml não encontrado nos resources.");
            }

            File tempFile = File.createTempFile("haarcascade", ".xml");
            Files.copy(is, tempFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            FACE_DETECTOR = new CascadeClassifier(tempFile.getAbsolutePath());
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Erro ao carregar Haar Cascade: " + e.getMessage());
        }
    }

    public BiometriaFacial salvarBiometria(MultipartFile file) throws Exception {
        byte[] imagemBytes = file.getBytes();
        validarImagem(imagemBytes, file.getContentType());
        BiometriaFacial biometriaFacial = validarMetadados(imagemBytes, file);
        validarImagemFraudulenta(imagemBytes, file.getContentType(), biometriaFacial);
        return biometriaFacialRepository.save(biometriaFacial);
    }

    private void validarImagem(byte[] imagemBytes, String contentType) throws Exception {
        if (!TIPOS_PERMITIDOS.contains(contentType)) {
            throw new Exception("Formato não permitido. Use JPEG ou PNG.");
        }

        if (imagemBytes.length > TAMANHO_MAX) {
            throw new Exception("Tamanho máximo de imagem excedido (5MB).");
        }

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imagemBytes));
        if (image == null) {
            throw new Exception("Imagem inválida.");
        }

        if (image.getWidth() < LARGURA_MIN || image.getHeight() < ALTURA_MIN) {
            throw new Exception("Dimensão mínima: 200x200 pixels.");
        }
    }

    private BiometriaFacial validarMetadados(byte[] imagemBytes, MultipartFile file) throws Exception {
        Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(imagemBytes));

        ExifSubIFDDirectory exif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        Date dataCaptura = exif != null ? exif.getDateOriginal() : null;

        ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        String fabricante = ifd0 != null ? ifd0.getString(ExifIFD0Directory.TAG_MAKE) : null;
        String modelo = ifd0 != null ? ifd0.getString(ExifIFD0Directory.TAG_MODEL) : null;

        GpsDirectory gpsDir = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        String gps = gpsDir != null && gpsDir.getGeoLocation() != null
                ? gpsDir.getGeoLocation().toString()
                : "Sem localização";

        BiometriaFacial biometriaFacial = new BiometriaFacial();
        biometriaFacial.setNomeArquivo(file.getOriginalFilename());
        biometriaFacial.setConteudo(new Binary(imagemBytes));
        biometriaFacial.setDataCaptura(dataCaptura);
        biometriaFacial.setFabricante(fabricante);
        biometriaFacial.setModelo(modelo);
        biometriaFacial.setGeoLocation(gps);
        return biometriaFacial;
    }

    private void validarImagemFraudulenta(byte[] imagemBytes, String contentType, BiometriaFacial biometriaFacial) throws Exception {
        // Define extensão com base no tipo MIME
        String tipoFraude = null;
        String extensao;
        switch (contentType) {
            case "image/jpeg": extensao = ".jpg"; break;
            case "image/png": extensao = ".png"; break;
            default: throw new Exception("Formato não suportado para validação com OpenCV.");
        }
        File tempFile = File.createTempFile("upload_", extensao);

        try {
            // Salva os bytes da imagem no arquivo temporário
            java.nio.file.Files.write(tempFile.toPath(), imagemBytes);
            System.out.println("Arquivo temporário salvo em: " + tempFile.getAbsolutePath());

            // Lê a imagem com OpenCV
            Mat imagem = Imgcodecs.imread(tempFile.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
            if (imagem.empty()) {
                throw new IOException("Erro ao processar imagem com OpenCV. Verifique se o arquivo é válido.");
            }

            // Aplica o filtro de Laplaciano para detectar complexidade
            double variancia = calcularVariancia(imagem);
            // Detecta rostos
            MatOfRect faces = detectaRosto(imagem);
            tipoFraude = detectarFraude(variancia, faces);

            if (tipoFraude != null) {
                NotificacaoFraudeRequest request = NotificacaoFraudeRequest.criarNotificacao(biometriaFacial, tipoFraude, "biometria-facial");
                NotificacaoFraudeUtil.notificarFraude(request);
            }
        } finally {
            tempFile.delete(); // garante remoção do arquivo temporário
        }
    }
    private double calcularVariancia(Mat imagem){
        Mat laplaciano = new Mat();
        Imgproc.Laplacian(imagem, laplaciano, CvType.CV_64F);
        MatOfDouble mean = new MatOfDouble();
        MatOfDouble stddev = new MatOfDouble();
        Core.meanStdDev(laplaciano, mean, stddev);
        double variancia = stddev.get(0, 0)[0];
        System.out.println("Variância da imagem: " + variancia);
        return variancia;
    }

    private MatOfRect detectaRosto(Mat imagem){
        MatOfRect faces = new MatOfRect();
        FACE_DETECTOR.detectMultiScale(imagem, faces);
        int numRostos = faces.toArray().length;
        System.out.println("Rostos detectados: " + numRostos);
        return faces;
    }

    private String detectarFraude(double variancia, MatOfRect faces) {
        String tipoFraude = null;
        // Heurística: baixa variação indica imagem artificial/suspeita
        if (variancia < 10.0) {
            tipoFraude = "Imagem com baixa complexidade visual. Possível fraude (foto de tela, papel ou máscara)";
        } else if (faces.empty()) {
            tipoFraude = "Nenhum rosto detectado na imagem. Possível tentativa de fraude.";
        }
        return tipoFraude;
    }
}
