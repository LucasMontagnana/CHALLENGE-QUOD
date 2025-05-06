package br.com.fiap.QUOD.controller;

import br.com.fiap.QUOD.model.Documento;
import br.com.fiap.QUOD.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DocumentoController {

    @Autowired
    private DocumentoService service;

    @PostMapping("/documentos")
    @ResponseStatus(HttpStatus.CREATED)
    public Documento gravar(@RequestParam("nome") String nome,
                            @RequestParam("extensao") String extensao,
                            @RequestParam("dataEnvio") String dataEnvio,
                            @RequestParam("arquivo") MultipartFile arquivo) throws IOException {
        // Converte o arquivo em byte[] e cria o Documento
        Documento documento = new Documento();
        documento.setNome(nome);
        documento.setExtensao(extensao);
        documento.setDataEnvio(LocalDate.parse(dataEnvio));
        documento.setConteudo(arquivo.getBytes()); // aqui está o conteúdo do arquivo em byte[]

        return service.gravar(documento); // salvar o documento no banco
    }

    @GetMapping("/documentos")
    @ResponseStatus(HttpStatus.OK)
    public List<Documento> listarTodosDocumentos() {
        return service.listarTodosOsDocumentos();
    }
}
