package br.com.cannamiranda.vehicles_api.model;

public record Veiculo(String placa,String chassi, String modelo, String marca, int anoFabricacao, int kilometragemRodada, TipoVeiculo tipo) {
}
