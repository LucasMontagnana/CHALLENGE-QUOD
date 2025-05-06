package br.com.fiap.QUOD.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.Date;
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
    private Date dataCaptura;
    private String modelo;
    private String fabricante;
    private String geoLocation;



    // Getters e setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public Binary getConteudo() { return conteudo; }
    public void setConteudo(Binary conteudo) { this.conteudo = conteudo; }

    public Date getDataCaptura() {
        return dataCaptura;
    }

    public void setDataCaptura(Date dataCaptura) {
        this.dataCaptura = dataCaptura;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    @Override
    public String toString() {
        return "Biometria{" +
                "id='" + id + '\'' +
                ", nomeArquivo='" + nomeArquivo + '\'' +
                ", conteudo=" + conteudo +
                ", dataCaptura=" + dataCaptura +
                ", modelo='" + modelo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", geoLocation='" + geoLocation + '\'' +
                '}';
    }
}
