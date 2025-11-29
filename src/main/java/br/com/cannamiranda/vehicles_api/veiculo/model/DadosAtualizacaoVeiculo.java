package br.com.cannamiranda.vehicles_api.veiculo.model;

public record DadosAtualizacaoVeiculo(
        Long id,
        String modelo,
        String marca,
        int kilometragem) {
}
