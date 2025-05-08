package br.com.fiap.QUOD.controller;

import br.com.fiap.QUOD.model.BiometriaFacial;
import br.com.fiap.QUOD.model.Documento;
import br.com.fiap.QUOD.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @PostMapping("/documentos")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> uploadDocumento(@RequestParam("file") MultipartFile arquivo) throws IOException {
        // Converte o arquivo em byte[] e cria o Documento
        Documento documento = null;
        try {
            documento = documentoService.gravar(arquivo);
            return ResponseEntity.ok("Documento salvo com ID: " + documento.getId() + "Documento: " + documento.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar documento: " + e.getMessage());
        }
    }

    @GetMapping("/documentos")
    @ResponseStatus(HttpStatus.OK)
    public List<Documento> listarTodosDocumentos() {
        return documentoService.listarTodosOsDocumentos();
    }
}
