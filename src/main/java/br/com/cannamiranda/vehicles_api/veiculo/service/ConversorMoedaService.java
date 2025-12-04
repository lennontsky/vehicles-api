package br.com.cannamiranda.vehicles_api.veiculo.service;

import br.com.cannamiranda.vehicles_api.veiculo.model.CambioResponse;
import br.com.cannamiranda.vehicles_api.veiculo.model.FallbackResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Getter
@Setter
public class ConversorMoedaService {


    @Value("${awsome-api.url}")
    private String apiUrl;

    @Value("${awsome-api.key}")
    private String accessKey;

    @Value("${fallback-api.url}")
    private String fallbackApiUrl;

    RestTemplate restTemplate = new RestTemplate();

    public Double converterRealParaDolar() {

        try {
            return obterCotacaoDaAwsomeApi();
        } catch (Exception e) {
            System.out.println("LOG: ERRO AO CONECTAR NA AWSOME API, USANDO FALLBACK");
            return obterCotacaoFallback();
        }


    }

    private Double obterCotacaoDaAwsomeApi() {

        try {
            String url = this.getApiUrl().toString() + "?token=" + this.getAccessKey().toString();
            System.out.println("LOG AWSOME API REQUEST URL: " + url);
            ResponseEntity<CambioResponse> response = restTemplate.getForEntity(url, CambioResponse.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody().getBid() == null) {
                System.out.println("LOG: AWSOME API INDISPONIVEL, USANDO FALLBACK");
                return obterCotacaoFallback();

            }

            return response.getBody().getBid();

        }
        catch (Exception e) {
            throw new RuntimeException("Erro ao conectar na AWSOME API", e);

        }

    }

    private Double obterCotacaoFallback() {

        if(fallbackApiUrl == null){
            this.setFallbackApiUrl("https://api.frankfurter.app/latest?from=USD&to=BRL");
        }

        ResponseEntity<FallbackResponse> response = restTemplate.getForEntity(fallbackApiUrl, FallbackResponse.class);
        return response.getBody().getRates().get("BRL");

    }
}
