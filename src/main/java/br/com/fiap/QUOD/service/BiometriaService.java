package br.com.fiap.QUOD.service;
import br.com.fiap.QUOD.model.Biometria;
import br.com.fiap.QUOD.repository.BiometriaRepository;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BiometriaService {
    @Autowired
    private BiometriaRepository biometriaRepository;

    public Biometria salvarBiometria(MultipartFile file) throws Exception {
        Biometria biometria = new Biometria();
        biometria.setNomeArquivo(file.getOriginalFilename());
        biometria.setConteudo(new Binary(file.getBytes()));
        return biometriaRepository.save(biometria);
    }
}
