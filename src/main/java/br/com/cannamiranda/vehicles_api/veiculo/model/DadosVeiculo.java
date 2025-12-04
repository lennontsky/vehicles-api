package br.com.cannamiranda.vehicles_api.veiculo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosVeiculo(

        @NotBlank
        String placa,

        @NotBlank
        String chassi,

        @NotBlank
        String modelo,

        @NotBlank
        String marca,

        @NotNull
        int ano,

        @NotNull
        int kilometragem,

        @NotNull
        TipoVeiculo tipo,

        @NotNull
        Double preco,

        Boolean ativo,

        LocalDateTime ultimaAtualizacaoPreco,

        String moeda

        ) { }
