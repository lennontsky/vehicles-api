package br.com.cannamiranda.vehicles_api.veiculo.service;

import br.com.cannamiranda.vehicles_api.veiculo.model.CambioResponse;
import br.com.cannamiranda.vehicles_api.veiculo.model.FallbackResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversorMoedaServiceTest {

    private ConversorMoedaService service;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        service = new ConversorMoedaService();
        // injeta o RestTemplate mockado no campo privado
        ReflectionTestUtils.setField(service, "restTemplate", restTemplate);
    }

    @Test
    void converterRealParaDolar_deveRetornarBidDaAwsomeApi_quando2xxEComBid() {
        service.setApiUrl("http://awsome");
        service.setAccessKey("KEY");

        CambioResponse cambio = mock(CambioResponse.class);
        when(cambio.getBid()).thenReturn(0.5);

        String expectedUrl = "http://awsome?token=KEY";
        when(restTemplate.getForEntity(expectedUrl, CambioResponse.class))
                .thenReturn(ResponseEntity.ok(cambio));

        Double resultado = service.converterRealParaDolar();

        assertNotNull(resultado);
        assertEquals(0.5, resultado);
        verify(restTemplate).getForEntity(expectedUrl, CambioResponse.class);
    }

    @Test
    void converterRealParaDolar_deveUsarFallback_quandoAwsomeRetornaBidNulo() {
        service.setApiUrl("http://awsome2");
        service.setAccessKey("KEY2");
        service.setFallbackApiUrl("http://fallback.test/latest");

        CambioResponse cambioNulo = mock(CambioResponse.class);
        when(cambioNulo.getBid()).thenReturn(null);

        String awsomeUrl = "http://awsome2?token=KEY2";
        when(restTemplate.getForEntity(awsomeUrl, CambioResponse.class))
                .thenReturn(ResponseEntity.ok(cambioNulo));

        FallbackResponse fallbackResp = mock(FallbackResponse.class);
        when(fallbackResp.getRates()).thenReturn(Map.of("BRL", 0.23));
        when(restTemplate.getForEntity("http://fallback.test/latest", FallbackResponse.class))
                .thenReturn(ResponseEntity.ok(fallbackResp));

        Double resultado = service.converterRealParaDolar();

        assertEquals(0.23, resultado);
        verify(restTemplate).getForEntity(awsomeUrl, CambioResponse.class);
        verify(restTemplate).getForEntity("http://fallback.test/latest", FallbackResponse.class);
    }

    @Test
    void converterRealParaDolar_deveUsarFallback_quandoAwsomeLancaExcecao() {
        service.setApiUrl("http://awsome3");
        service.setAccessKey("KEY3");
        service.setFallbackApiUrl("http://fallback2.test/latest");

        String awsomeUrl = "http://awsome3?token=KEY3";
        when(restTemplate.getForEntity(awsomeUrl, CambioResponse.class))
                .thenThrow(new RuntimeException("conn error"));

        FallbackResponse fallbackResp = mock(FallbackResponse.class);
        when(fallbackResp.getRates()).thenReturn(Map.of("BRL", 0.42));
        when(restTemplate.getForEntity("http://fallback2.test/latest", FallbackResponse.class))
                .thenReturn(ResponseEntity.ok(fallbackResp));

        Double resultado = service.converterRealParaDolar();

        assertEquals(0.42, resultado);
        verify(restTemplate).getForEntity(awsomeUrl, CambioResponse.class);
        verify(restTemplate).getForEntity("http://fallback2.test/latest", FallbackResponse.class);
    }

    @Test
    void obterCotacaoFallback_deveSetarUrlPadrao_quandoFallbackApiUrlNull() {
        // Deixa fallbackApiUrl null para forçar atribuição do padrão dentro do método
        service.setApiUrl("http://awsome4");
        service.setAccessKey("KEY4");
        service.setFallbackApiUrl(null);

        String awsomeUrl = "http://awsome4?token=KEY4";
        when(restTemplate.getForEntity(awsomeUrl, CambioResponse.class))
                .thenThrow(new RuntimeException("force fallback"));

        FallbackResponse fallbackResp = mock(FallbackResponse.class);
        when(fallbackResp.getRates()).thenReturn(Map.of("BRL", 0.77));
        String defaultFallback = "https://api.frankfurter.app/latest?from=USD&to=BRL";
        when(restTemplate.getForEntity(defaultFallback, FallbackResponse.class))
                .thenReturn(ResponseEntity.ok(fallbackResp));

        Double resultado = service.converterRealParaDolar();

        assertEquals(0.77, resultado);
        // confirmou que a URL padrão foi atribuída ao campo
        assertEquals(defaultFallback, service.getFallbackApiUrl());
        verify(restTemplate).getForEntity(defaultFallback, FallbackResponse.class);
    }
}