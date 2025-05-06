package br.com.fiap.QUOD.util;

import br.com.fiap.QUOD.dto.NotificacaoFraudeRequest;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

public class NotificacaoFraudeUtil {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String url = "http://localhost:8080/api/notificacoes/fraude";

    public static void notificarFraude(NotificacaoFraudeRequest request){
        restTemplate.postForEntity(url, request, Void.class);
    }
}
