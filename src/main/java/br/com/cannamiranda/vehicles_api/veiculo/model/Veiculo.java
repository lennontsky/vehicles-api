package br.com.cannamiranda.vehicles_api.veiculo.model;

import br.com.cannamiranda.vehicles_api.veiculo.service.ConversorMoedaService;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Table(name = "veiculos")
@Entity(name = "Veiculo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String placa;
    private String chassi;
    private String modelo;
    private String marca;
    private int ano;
    private int kilometragem;

    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipo;
    private Boolean ativo;
    private Double preco;

    private String moeda;
    private LocalDateTime ultimaAtualizacao;


    public Veiculo(@Valid DadosVeiculo dadosVeiculo) {
        this.placa = dadosVeiculo.placa();
        this.chassi = dadosVeiculo.chassi();
        this.modelo = dadosVeiculo.modelo();
        this.marca = dadosVeiculo.marca();
        this.ano = dadosVeiculo.ano();
        this.kilometragem = dadosVeiculo.kilometragem();
        this.tipo = dadosVeiculo.tipo();
        this.moeda = dadosVeiculo.moeda();
        this.ativo = true;

        //TODO: Issue #8 Refactor - A lógica abaixo seria interessanter de mover pra outro lugar, talvez um service...
        if (precisaAtualizarCotacaoDoPreco()) {
            ConversorMoedaService service = new ConversorMoedaService();
            this.preco = service.converterRealParaDolar();
            this.moeda = "USD";
        } else {
            this.preco = dadosVeiculo.preco();
            this.moeda = dadosVeiculo.moeda();
        }

    }


    private boolean precisaAtualizarCotacaoDoPreco() {

        if (this.moeda == null) {
            return true;
        }

        switch (this.moeda){

            case "USD":
                return false;

            default:
                return true;

        }
    }


    public Veiculo atualizarInformacoes(Veiculo veiculoDB) {

        if(!this.equals(veiculoDB)) {
            if (veiculoDB.kilometragem != this.kilometragem) {
                System.out.println("LOG: Atualizando kilometragem de " + this.kilometragem + " para " + veiculoDB.kilometragem);
                this.kilometragem = veiculoDB.kilometragem;
            }
            if (!veiculoDB.preco.equals(this.preco)) {
                System.out.println("LOG: Atualizando preço de " + this.preco + " para " + veiculoDB.preco);
                this.preco = veiculoDB.preco;
            }
            if (!veiculoDB.getAtivo()) {
                System.out.println("LOG: Atualizando status de ativo de " + this.ativo + " para " + veiculoDB.getAtivo());
                this.ativo = veiculoDB.getAtivo();
            }

            return this;
        }
        else{
            return null;
        }

    }

        public void setAtivo(boolean b) {
        this.ativo = b;
    }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Veiculo veiculo = (Veiculo) o;
            return id == veiculo.id
                    && ano == veiculo.ano
                    && kilometragem == veiculo.kilometragem
                    && java.util.Objects.equals(placa, veiculo.placa)
                    && java.util.Objects.equals(chassi, veiculo.chassi)
                    && java.util.Objects.equals(modelo, veiculo.modelo)
                    && java.util.Objects.equals(marca, veiculo.marca)
                    && java.util.Objects.equals(tipo, veiculo.tipo)
                    && java.util.Objects.equals(ativo, veiculo.ativo)
                    && java.util.Objects.equals(preco, veiculo.preco);
        }

}
