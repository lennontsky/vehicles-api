package br.com.cannamiranda.vehicles_api.exceptions;

public class PlacaRepetidaException extends RuntimeException {

    String placa;
    String message;

    public PlacaRepetidaException(String placa) {
        this.message = "A placa " + placa + " já está cadastrada no sistema.";
        this.placa = placa;
    }
}
