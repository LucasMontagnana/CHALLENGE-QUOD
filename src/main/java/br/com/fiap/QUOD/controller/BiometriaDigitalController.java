package br.com.fiap.QUOD.controller;

import br.com.fiap.QUOD.service.BiometriaDigitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/biometria-digital")
public class BiometriaDigitalController {

    @Autowired
    private BiometriaDigitalService service;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrar(@RequestParam("arquivo") MultipartFile arquivo) throws Exception {
        String id = service.salvar(arquivo);
        return ResponseEntity.ok("Biometria digital salva com ID: " + id);
    }

    @PostMapping("/comparar/{id}")
    public ResponseEntity<String> comparar(@PathVariable String id,
                                           @RequestParam("arquivo") MultipartFile arquivo) throws Exception {
        boolean resultado = service.comparar(id, arquivo);
        return ResponseEntity.ok("Biometrias são " + (resultado ? "compatíveis" : "diferentes"));
    }
}
