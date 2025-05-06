package br.com.fiap.QUOD.service;
import br.com.fiap.QUOD.model.Biometria;
import br.com.fiap.QUOD.repository.BiometriaRepository;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;

import java.io.File;
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
    static {
        nu.pattern.OpenCV.loadLocally(); // se usar o wrapper openpnp
    }

    @Autowired
    private BiometriaRepository biometriaRepository;

    public Biometria salvarBiometria(MultipartFile file) throws Exception {
        validarImagem(file);
        Biometria biometria = validarMetadados(file);
        return biometriaRepository.save(biometria);
    }

    private void validarImagem(MultipartFile file) throws Exception {
        if (!TIPOS_PERMITIDOS.contains(file.getContentType())) {
            throw new Exception("Formato não permitido. Use JPEG ou PNG.");
        }

        if (file.getSize() > TAMANHO_MAX) {
            throw new Exception("Tamanho máximo de imagem excedido (5MB).");
        }

        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new Exception("Imagem inválida.");
        }

        if (image.getWidth() < LARGURA_MIN || image.getHeight() < ALTURA_MIN) {
            throw new Exception("Dimensão mínima: 200x200 pixels.");
        }

    }

    private Biometria validarMetadados(MultipartFile file) throws Exception {
        Metadata metadata = ImageMetadataReader.readMetadata(file.getInputStream());

        // Data de captura
        ExifSubIFDDirectory exif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        Date dataCaptura = exif != null ? exif.getDateOriginal() : null;

        // Fabricante e modelo
        ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        String fabricante = ifd0 != null ? ifd0.getString(ExifIFD0Directory.TAG_MAKE) : null;
        String modelo = ifd0 != null ? ifd0.getString(ExifIFD0Directory.TAG_MODEL) : null;

        // GPS
        GpsDirectory gpsDir = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        String gps = gpsDir != null && gpsDir.getGeoLocation() != null
                ? gpsDir.getGeoLocation().toString()
                : "Sem localização";

        // Impressão (pode ser log, salvar, ou validar)
        Biometria biometria = new Biometria();
        biometria.setNomeArquivo(file.getOriginalFilename());
        biometria.setConteudo(new Binary(file.getBytes()));
        biometria.setDataCaptura(dataCaptura);
        biometria.setFabricante(fabricante);
        biometria.setModelo(modelo);
        biometria.setGeoLocation(gps);
        return biometria;
    }

    private void validarImagemFraudulenta(MultipartFile file) throws IOException, Exception {
        // Salva temporariamente para leitura com OpenCV
        File tempFile = File.createTempFile("upload", ".tmp");
        file.transferTo(tempFile);

        // Lê a imagem em escala de cinza
        Mat imagem = Imgcodecs.imread(tempFile.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);

        // Aplica o filtro de Laplaciano (detecta bordas)
        Mat laplaciano = new Mat();
        Imgproc.Laplacian(imagem, laplaciano, CvType.CV_64F);

        // Calcula a variação
        MatOfDouble mean = new MatOfDouble();
        MatOfDouble stddev = new MatOfDouble();
        Core.meanStdDev(laplaciano, mean, stddev);

        double variancia = stddev.get(0, 0)[0];

        // Heurística: se a variação for muito baixa, pode ser fraude (foto de tela ou papel)
        if (variancia < 10.0) {
            throw new Exception("Imagem com baixa complexidade visual. Possível tentativa de fraude (foto de foto ou impressão).");
        }

        // Apaga arquivo temporário
        tempFile.delete();
    }
}
