package br.com.fiap.QUOD.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.UUID;

@Data
@Document(collection = "biometria-digital")
public class BiometriaDigital {
    @Id
    private String id = UUID.randomUUID().toString();
    private byte[] imagemDigital;
    private String descricao;
}
