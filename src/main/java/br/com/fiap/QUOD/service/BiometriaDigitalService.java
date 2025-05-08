package br.com.fiap.QUOD.service;

import br.com.fiap.QUOD.model.BiometriaDigital;
import br.com.fiap.QUOD.repository.BiometriaDigitalRepository;
import com.machinezoo.sourceafis.FingerprintTemplate;
import com.machinezoo.sourceafis.FingerprintMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BiometriaDigitalService {

    @Autowired
    private BiometriaDigitalRepository repository;

    public String salvar(MultipartFile file) throws Exception {
        BiometriaDigital biometria = new BiometriaDigital();
        biometria.setImagemDigital(file.getBytes());
        return repository.save(biometria).getId();
    }

    public boolean comparar(String idExistente, MultipartFile novaDigital) throws Exception {
        BiometriaDigital existente = repository.findById(idExistente)
                .orElseThrow(() -> new RuntimeException("ID não encontrado"));

        FingerprintTemplate templateExistente = new FingerprintTemplate()
                .dpi(500)
                .create(existente.getImagemDigital());

        FingerprintTemplate templateNovo = new FingerprintTemplate()
                .dpi(500)
                .create(novaDigital.getBytes());

        double score = new FingerprintMatcher()
                .index(templateExistente)
                .match(templateNovo);

        return score >= 40; // limiar recomendado
    }
}
