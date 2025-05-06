package br.com.fiap.QUOD.controller;

import br.com.fiap.QUOD.model.Documento;
import br.com.fiap.QUOD.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DocumentoController {

    @Autowired
    private DocumentoService service;

    @PostMapping("/documentos")
    @ResponseStatus(HttpStatus.CREATED)
    public Documento gravar(@RequestBody Documento documento){
        return service.gravar(documento);
    }

    @GetMapping("/documentos")
    @ResponseStatus(HttpStatus.OK)
    public List<Documento> listarTodosDocumentos(){
        return service.listarTodosOsDocumentos();
    }

//    @DeleteMapping("/documentos/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void excluir(@PathVariable Long id){
//        service.excluir(id);
//    }

//    @PutMapping("/documentos")
//    @ResponseStatus(HttpStatus.OK)
//    public Documento atualizar(@RequestBody Documento documento){
//        return service.atualizar(documento);
//    }

//    @GetMapping("/documentos/{nome}")
//    @ResponseStatus(HttpStatus.OK)
//    public Documento buscarDocumentoPeloNome(@PathVariable String nome){
//        return service.buscasPeloNome(nome);
//    }

//    @GetMapping("/documentos/{dataInicial}/{dataFinal}")
//    @ResponseStatus(HttpStatus.OK)
//    public List<Documento> listarPorDataEnvio(@PathVariable LocalDate dataInicial,
//                                              @PathVariable LocalDate dataFinal){
//        return service.listarPorDataEnvio(dataInicial, dataFinal);
//    }
}
