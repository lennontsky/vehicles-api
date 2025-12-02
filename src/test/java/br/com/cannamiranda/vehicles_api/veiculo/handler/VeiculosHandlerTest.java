package br.com.cannamiranda.vehicles_api.veiculo.handler;

import br.com.cannamiranda.vehicles_api.exceptions.PlacaRepetidaException;
import br.com.cannamiranda.vehicles_api.exceptions.VeiculoInexistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class VeiculosHandlerTest {

    private VeiculosHandler handler;

    @BeforeEach
    void setup() {
        handler = new VeiculosHandler();
    }

    @Test
    void handlePlacaRepetidaException_retornaBadRequestComMensagem() {
        PlacaRepetidaException ex = new PlacaRepetidaException("Placa já cadastrada");

        ResponseEntity<String> resp = handler.handlePlacaRepetidaException(ex);

        assertNotNull(resp);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals("Placa já cadastrada", resp.getBody());
    }

    @Test
    void handlerNotFound_retornaNotFoundComMsg() {
        VeiculoInexistente ex = new VeiculoInexistente("Veículo não encontrado");

        ResponseEntity<String> resp = handler.handlerNotFound(ex);

        assertNotNull(resp);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertEquals("Veículo não encontrado", resp.getBody());
    }

    @Test
    void handlerNotFound_comMsgNula_retornaNotFoundComBodyNulo() {
        VeiculoInexistente ex = new VeiculoInexistente("Veiculo não encontrado") {;
            @Override
            public String getMsg() {
                return null;
            }
        };

        ResponseEntity<String> resp = handler.handlerNotFound(ex);

        assertNotNull(resp);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertNull(resp.getBody());
    }
}