package br.com.cannamiranda.vehicles_api.veiculo.service;

import br.com.cannamiranda.vehicles_api.veiculo.model.CambioResponse;
import br.com.cannamiranda.vehicles_api.veiculo.model.FallbackResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ConversorMoedaService {


    @Value("${awsome-api.url}")
    private String apiUrl;

    @Value("${awsome-api.key}")
    private String accessKey;

    @Value("${fallback-api.url}")
    private String fallbackApiUrl;

    RestTemplate restTemplate = new RestTemplate();

    public Double converterRealParaDolar() {

        apiUrl += "?token=" + accessKey;

        System.out.println("API URL: " + apiUrl);
        ResponseEntity<CambioResponse> response = restTemplate.getForEntity(apiUrl, CambioResponse.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody().getBid() == null) {
            //throw new AwsomeApiIndisponivel("Erro ao obter a taxa de câmbio.");
            //chamar método de fallback
            System.out.println("AWSOME API INDISPONIVEL, USANDO FALLBACK");
            return obterCotacaoFallback();

        }
        else {

            System.out.println(response);

            CambioResponse cambioResponse = response.getBody();
            Double taxaDeCambio = cambioResponse.getBid();

            //double taxaDeCambio = response.getBody().getBid();
            return taxaDeCambio;
        }

    }

    private Double obterCotacaoFallback() {
        ResponseEntity<FallbackResponse> response = restTemplate.getForEntity(fallbackApiUrl, FallbackResponse.class);
        return response.getBody().getRates().get("BRL");
    }
}
