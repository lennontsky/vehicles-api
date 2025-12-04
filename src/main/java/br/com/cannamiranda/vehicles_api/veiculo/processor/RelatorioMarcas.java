package br.com.cannamiranda.vehicles_api.veiculo.processor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RelatorioMarcas {
    String marca;
    long total;

//    public RelatorioMarcas(String marca, Number total) {
//        this.marca = marca;
//        this.total = total == null ? 0L : total.longValue();
//    }

}


