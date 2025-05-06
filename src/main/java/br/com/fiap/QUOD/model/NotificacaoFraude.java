package br.com.fiap.QUOD.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Document(collection = "notificacoes_fraude")
public class NotificacaoFraude {
    @Id
    private String transacaoId;
    private String tipoBiometria;
    private String tipoFraude;
    private Date dataCaptura;
    private Dispositivo dispositivo;
    private List<String> canalNotificacao;
    private String notificadoPor;
    private Map<String, Object> metadados;

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
