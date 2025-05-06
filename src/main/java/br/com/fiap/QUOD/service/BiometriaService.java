package br.com.fiap.QUOD.service;
import br.com.fiap.QUOD.model.Biometria;
import br.com.fiap.QUOD.repository.BiometriaRepository;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class BiometriaService {

    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList("image/jpeg", "image/png");
    private static final long TAMANHO_MAX = 5 * 1024 * 1024; // 5MB
    private static final int LARGURA_MIN = 200;
    private static final int ALTURA_MIN = 200;

    @Autowired
    private BiometriaRepository biometriaRepository;

    public Biometria salvarBiometria(MultipartFile file) throws Exception {
        validarImagem(file);
        Biometria biometria = new Biometria();
        biometria.setNomeArquivo(file.getOriginalFilename());
        biometria.setConteudo(new Binary(file.getBytes()));
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

        // Aqui você pode incluir validações de EXIF se desejar
    }
}
