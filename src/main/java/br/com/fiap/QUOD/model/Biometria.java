package br.com.fiap.QUOD.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "biometria")
public class Biometria {

    @Id
    private String id = UUID.randomUUID().toString();
    private String nomeArquivo;
    private Binary conteudo;

    // Getters e setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public Binary getConteudo() { return conteudo; }
    public void setConteudo(Binary conteudo) { this.conteudo = conteudo; }
}
