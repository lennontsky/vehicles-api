package br.com.cannamiranda.vehicles_api.veiculo.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class FallbackResponse {

    private double amount;
    private String base;
    private String date;
    private Map<String, Double> rates;

}
