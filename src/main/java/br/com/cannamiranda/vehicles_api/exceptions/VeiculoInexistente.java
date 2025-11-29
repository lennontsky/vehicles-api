package br.com.cannamiranda.vehicles_api.exceptions;

import org.springframework.http.HttpStatus;

public class VeiculoInexistente extends RuntimeException {

    HttpStatus status;
    private String message;

    public VeiculoInexistente(String message) {
        super(message);
        this.message = message;
        this.status = HttpStatus.NOT_FOUND;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
    public String getMsg() {
        return this.message;
    }
}