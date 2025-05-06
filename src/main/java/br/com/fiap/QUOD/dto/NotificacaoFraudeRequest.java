// NotificacaoFraudeRequest.java
package br.com.fiap.QUOD.dto;

import lombok.Data;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class NotificacaoFraudeRequest {
    private String transacaoId;
    private String tipoBiometria;
    private String tipoFraude;
    private ZonedDateTime dataCaptura;
    private Dispositivo dispositivo;
    private List<String> canalNotificacao;
    private String notificadoPor;
    private Metadados metadados;

    @Data
    public static class Dispositivo {
        private String fabricante;
        private String modelo;
        private String sistemaOperacional;
    }

    @Data
    public static class Metadados {
        private double latitude;
        private double longitude;
        private String ipOrigem;
    }
}
