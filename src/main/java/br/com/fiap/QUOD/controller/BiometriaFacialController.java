package br.com.fiap.QUOD.controller;
import br.com.fiap.QUOD.model.BiometriaFacial;
import br.com.fiap.QUOD.service.BiometriaFacialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/biometria-facial")
public class BiometriaFacialController {

    @Autowired
    private BiometriaFacialService biometriaFacialService;

    @PostMapping("/validar")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> uploadDocumento(@RequestParam("file") MultipartFile file) {
        try {
            BiometriaFacial biometriaFacialSalva = biometriaFacialService.salvarBiometria(file);
            return ResponseEntity.ok("Imagem salva com ID: " + biometriaFacialSalva.getId() + "Imagem: " + biometriaFacialSalva.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar imagem: " + e.getMessage());
        }
    }
}