package br.com.fiap.QUOD.util;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class DocumentoUtil {

    public static boolean validarQualidadeImagem(MultipartFile file) throws IOException {
        byte[] imagemBytes = file.getBytes();
        Mat imagem = Imgcodecs.imdecode(new MatOfByte(imagemBytes), Imgcodecs.IMREAD_GRAYSCALE);

        if (imagem.empty()) {
            throw new RuntimeException("Imagem inválida ou corrompida.");
        }

        // Nitidez (foco) com Laplacian
        Mat laplacian = new Mat();
        Imgproc.Laplacian(imagem, laplacian, CvType.CV_64F);
        MatOfDouble laplacianStdDev = new MatOfDouble();
        Core.meanStdDev(laplacian, new MatOfDouble(), laplacianStdDev);
        double foco = laplacianStdDev.get(0, 0)[0];

        // Brilho
        double brilho = Core.mean(imagem).val[0];

        // Contraste (desvio padrão)
        MatOfDouble contrasteStdDev = new MatOfDouble();
        Core.meanStdDev(imagem, new MatOfDouble(), contrasteStdDev);
        double contraste = contrasteStdDev.get(0, 0)[0];

        // Parâmetros mínimos para aprovação
        System.out.printf("foco" + foco + "brilho: " + brilho + "contrate: " + contraste);
        return foco > 35 && brilho > 70 && contraste > 30;
    }
}
