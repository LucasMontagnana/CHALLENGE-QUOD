package br.com.fiap.QUOD.service;

import br.com.fiap.QUOD.dto.NotificacaoFraudeRequest;
import br.com.fiap.QUOD.model.Documento;
import br.com.fiap.QUOD.repository.DocumentoRepository;
import br.com.fiap.QUOD.util.DocumentoUtil;
import br.com.fiap.QUOD.util.NotificacaoFraudeUtil;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    public Documento gravar(MultipartFile file) throws Exception {

        Documento documento = new Documento();
        documento.setConteudo(file.getBytes());
        documento.setDataEnvio(java.sql.Date.valueOf(LocalDate.now()));

        if (!DocumentoUtil.validarQualidadeImagem(file)) {
            NotificacaoFraudeRequest request = NotificacaoFraudeRequest.criarNotificacao(documento,
                    "Imagem do documento possui baixa qualidade (foco, brilho ou contraste insuficiente).");
            NotificacaoFraudeUtil.notificarFraude(request);
        }
        return documentoRepository.save(documento);
    }

    public List<Documento> listarTodosOsDocumentos(){
        return documentoRepository.findAll();
    }
}