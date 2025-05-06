package br.com.fiap.QUOD.service;
import br.com.fiap.QUOD.model.Biometria;
import br.com.fiap.QUOD.repository.BiometriaRepository;
import org.bson.types.Binary;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

@Service
public class BiometriaService {

    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList("image/jpeg", "image/png");
    private static final long TAMANHO_MAX = 5 * 1024 * 1024; // 5MB
    private static final int LARGURA_MIN = 200;
    private static final int ALTURA_MIN = 200;
    private static final CascadeClassifier FACE_DETECTOR;

    static {
        nu.pattern.OpenCV.loadLocally(); // se usar o wrapper openpnp
    }

    static {
        try (InputStream is = BiometriaService.class.getClassLoader()
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

    @Autowired
    private BiometriaRepository biometriaRepository;

    public Biometria salvarBiometria(MultipartFile file) throws Exception {
        byte[] imagemBytes = file.getBytes(); // lê uma vez só
        validarImagem(imagemBytes, file.getContentType());
        validarImagemFraudulenta(imagemBytes, file.getContentType()); // opcional
        Biometria biometria = validarMetadados(imagemBytes, file);
        return biometriaRepository.save(biometria);
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


    private Biometria validarMetadados(byte[] imagemBytes, MultipartFile file) throws Exception {
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

        Biometria biometria = new Biometria();
        biometria.setNomeArquivo(file.getOriginalFilename());
        biometria.setConteudo(new Binary(imagemBytes));
        biometria.setDataCaptura(dataCaptura);
        biometria.setFabricante(fabricante);
        biometria.setModelo(modelo);
        biometria.setGeoLocation(gps);
        return biometria;
    }


    private void validarImagemFraudulenta(byte[] imagemBytes, String contentType) throws Exception {
        // Define extensão com base no tipo MIME
        String extensao;
        switch (contentType) {
            case "image/jpeg": extensao = ".jpg"; break;
            case "image/png": extensao = ".png"; break;
            default: throw new Exception("Formato não suportado para validação com OpenCV.");
        }

        // Cria arquivo temporário com a extensão correta
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
            Mat laplaciano = new Mat();
            Imgproc.Laplacian(imagem, laplaciano, CvType.CV_64F);

            MatOfDouble mean = new MatOfDouble();
            MatOfDouble stddev = new MatOfDouble();
            Core.meanStdDev(laplaciano, mean, stddev);

            double variancia = stddev.get(0, 0)[0];
            System.out.println("Variância da imagem: " + variancia);

            // Heurística: baixa variação indica imagem artificial/suspeita
            if (variancia < 10.0) {
                throw new Exception("Imagem com baixa complexidade visual. Possível fraude (foto de tela, papel ou máscara).");
            }

            // Detecta rostos
            MatOfRect faces = new MatOfRect();
            FACE_DETECTOR.detectMultiScale(imagem, faces);

            int numRostos = faces.toArray().length;
            System.out.println("Rostos detectados: " + numRostos);

            // Verifica se pelo menos um rosto foi encontrado
            if (faces.empty()) {
                throw new Exception("Nenhum rosto detectado na imagem. Possível tentativa de fraude.");
            } else if (numRostos > 1) {
                throw new Exception("Mais de um rosto detectado. Imagem inválida para biometria.");
            }

        } finally {
            tempFile.delete(); // garante remoção do arquivo temporário
        }
    }
}
