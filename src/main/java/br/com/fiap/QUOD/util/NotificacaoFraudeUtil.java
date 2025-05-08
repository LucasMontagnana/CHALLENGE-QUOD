package br.com.fiap.QUOD.util;

import br.com.fiap.QUOD.dto.NotificacaoFraudeRequest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

public class NotificacaoFraudeUtil {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String url = "http://localhost:8080/api/notificacoes/fraude";

    // Substitua com seu usuário e senha do Spring Security
    private static final String USERNAME = "user";
    private static final String PASSWORD = "1234";

    public static void notificarFraude(NotificacaoFraudeRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Basic Auth manual
        String auth = USERNAME + ":" + PASSWORD;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);

        HttpEntity<NotificacaoFraudeRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Void.class
            );
            System.out.println("Status da notificação de fraude: " + response.getStatusCode());
        } catch (Exception e) {
            System.err.println("Erro ao notificar fraude: " + e.getMessage());
        }
    }
}
