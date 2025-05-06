package br.com.fiap.QUOD.controller;

import br.com.fiap.QUOD.model.NotificacaoFraude;
import br.com.fiap.QUOD.service.NotificacaoFraudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificacoes/fraude")
public class NotificacaoFraudeController {

    @Autowired
    private NotificacaoFraudeService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NotificacaoFraude> criar(@RequestBody NotificacaoFraude notificacao) {
        NotificacaoFraude salva = service.salvar(notificacao);
        return ResponseEntity.ok(salva);
    }
}
