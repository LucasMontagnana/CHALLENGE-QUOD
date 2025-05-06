package br.com.fiap.QUOD.controller;
import br.com.fiap.QUOD.model.Biometria;
import br.com.fiap.QUOD.service.BiometriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class BiometriaController {

    @Autowired
    private BiometriaService biometriaService;

    @PostMapping("/biometria")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> uploadDocumento(@RequestParam("file") MultipartFile file) {
        try {
            Biometria biometriaSalva = biometriaService.salvarBiometria(file);
            return ResponseEntity.ok("Imagem salva com ID: " + biometriaSalva.getId() + "Imagem: " + biometriaSalva.toString());
        } catch (Exception e) {
            //return ResponseEntity.badRequest().body("Erro ao processar imagem: " + e.getMessage());
            return ResponseEntity.badRequest().body("Erro ao processar imagem: " + e.getMessage());
        }
    }
}