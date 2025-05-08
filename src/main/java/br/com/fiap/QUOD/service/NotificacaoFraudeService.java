package br.com.fiap.QUOD.service;

import br.com.fiap.QUOD.model.NotificacaoFraude;
import br.com.fiap.QUOD.repository.NotificacaoFraudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoFraudeService {

    @Autowired
    private NotificacaoFraudeRepository repository;

    public NotificacaoFraude salvar(NotificacaoFraude notificacao) {
        return repository.save(notificacao);
    }
}
