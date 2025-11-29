package br.com.cannamiranda.vehicles_api.veiculo.handler;

import br.com.cannamiranda.vehicles_api.exceptions.PlacaRepetidaException;
import br.com.cannamiranda.vehicles_api.exceptions.VeiculoInexistente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class VeiculosHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PlacaRepetidaException.class)
    public ResponseEntity<String> handlePlacaRepetidaException(PlacaRepetidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(VeiculoInexistente.class)
    public ResponseEntity<String> handlerNotFound(VeiculoInexistente ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMsg());
    }
}
