package br.com.fiap.QUOD.service;

import br.com.fiap.QUOD.model.Documento;
import br.com.fiap.QUOD.repository.DocumentoRepository;
import br.com.fiap.QUOD.util.DocumentoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    public Documento gravar(Documento documento, MultipartFile file) throws IOException {
        if (!DocumentoUtil.validarQualidadeImagem(file)) {
            throw new RuntimeException("Imagem do documento possui baixa qualidade (foco, brilho ou contraste insuficiente).");
        }
        return documentoRepository.save(documento);
    }

    public List<Documento> listarTodosOsDocumentos(){
        return documentoRepository.findAll();
    }
}

