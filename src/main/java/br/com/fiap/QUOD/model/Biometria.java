package br.com.fiap.QUOD.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Document(collection = "biometria")
public class Biometria {

    @Id
    private String id = UUID.randomUUID().toString();
    private String nome;
    private String extensao;
    private LocalDate dataEnvio;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }

    public LocalDate getDataEnvio() {
        return dataEnvio;
    }

    public void setData_envio(LocalDate dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Biometria documento = (Biometria) o;
        return Objects.equals(id, documento.id) && Objects.equals(nome, documento.nome) && Objects.equals(extensao, documento.extensao) && Objects.equals(dataEnvio, documento.dataEnvio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, extensao, dataEnvio);
    }
}
