package br.com.fiap.QUOD.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Document(collection = "documentos")
public class Documento {

    @Id
    private String id = UUID.randomUUID().toString();
    private String nome;
    private String extensao;
    private LocalDate dataEnvio;

    private byte[] conteudo;

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

    public void setDataEnvio(LocalDate dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    // 👉 Adicione estes métodos:
    public byte[] getConteudo() {
        return conteudo;
    }

    public void setConteudo(byte[] conteudo) {
        this.conteudo = conteudo; // Método para setar o conteúdo do arquivo
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Documento documento = (Documento) o;
        return Objects.equals(id, documento.id) && Objects.equals(nome, documento.nome) && Objects.equals(extensao, documento.extensao) && Objects.equals(dataEnvio, documento.dataEnvio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, extensao, dataEnvio);
    }
}
