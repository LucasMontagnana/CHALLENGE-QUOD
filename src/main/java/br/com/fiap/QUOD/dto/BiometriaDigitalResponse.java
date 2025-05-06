package br.com.fiap.QUOD.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BiometriaDigitalResponse {
    private String id;
    private boolean corresponde;
    private double pontuacao;
}
