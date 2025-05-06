package br.com.fiap.QUOD.controller;
import br.com.fiap.QUOD.model.Biometria;
import br.com.fiap.QUOD.service.BiometriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api2")
public class BiometriaController {

    @Autowired
    private BiometriaService biometriaService;

    @PostMapping("/biometria")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Biometria> uploadDocumento(@RequestParam("file") MultipartFile file) {
        try {
            Biometria biometriaSalva = biometriaService.salvarBiometria(file);
            return ResponseEntity.ok(biometriaSalva);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}