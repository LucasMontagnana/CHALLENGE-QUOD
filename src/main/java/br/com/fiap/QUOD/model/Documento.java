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
    private LocalDate dataEnvio;
    private byte[] conteudo;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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

}
