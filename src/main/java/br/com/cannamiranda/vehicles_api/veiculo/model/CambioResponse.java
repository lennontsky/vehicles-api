package br.com.cannamiranda.vehicles_api.veiculo.model;

import lombok.Getter;

import java.util.Date;

@Getter
public class CambioResponse{

    private String code;
    private String codein;
    private String name;
    private Double high;
    private Double low;
    private Double varBid;
    private Double pctChange;
    private Double bid;
    private String ask;
    private String timestamp;
    private Date create_date;

}
