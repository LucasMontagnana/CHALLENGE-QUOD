package br.com.fiap.QUOD.dto;
import br.com.fiap.QUOD.model.BiometriaFacial;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Data
public class NotificacaoFraudeRequest {
    private String transacaoId;
    private String tipoBiometria;
    private String tipoFraude;
    private Date dataCaptura;
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

    public static NotificacaoFraudeRequest criarNotificacao(BiometriaFacial biometria, String tipoFraude, String tipoBiometria) {
        NotificacaoFraudeRequest request = new NotificacaoFraudeRequest();
        request.setTransacaoId(biometria.getId());
        request.setTipoBiometria(tipoBiometria);
        request.setTipoFraude(tipoFraude);
        request.setDataCaptura(biometria.getDataCaptura());
        request.setNotificadoPor("sistema");
        request.setCanalNotificacao(List.of("email"));

        var dispositivo = new NotificacaoFraudeRequest.Dispositivo();
        dispositivo.setFabricante(biometria.getFabricante());
        dispositivo.setModelo(biometria.getModelo());
        dispositivo.setSistemaOperacional("Desconhecido");
        request.setDispositivo(dispositivo);

        var metadados = new NotificacaoFraudeRequest.Metadados();
        metadados.setLatitude(0D);
        metadados.setLongitude(0D);
        metadados.setIpOrigem("192.168.0.1");
        request.setMetadados(metadados);

        return request;
    }
}
