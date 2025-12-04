package br.com.cannamiranda.vehicles_api.veiculo.model;

import jdk.jshell.Snippet;

public record DadosAtualizacaoVeiculo(
        Long id,
        String modelo,
        String marca,
        int kilometragem) {}
