package br.com.fiap.QUOD.service;

import br.com.fiap.QUOD.model.Documento;
import br.com.fiap.QUOD.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    public Documento gravar(Documento documento){
        return documentoRepository.save(documento);
    }

    public List<Documento> listarTodosOsDocumentos(){
        return documentoRepository.findAll();
    }

}
