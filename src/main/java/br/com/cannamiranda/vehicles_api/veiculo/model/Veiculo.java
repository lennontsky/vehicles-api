package br.com.cannamiranda.vehicles_api.veiculo.model;

import br.com.cannamiranda.vehicles_api.veiculo.service.ConversorMoedaService;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDateTime;

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
            double cotacao = service.obtemCotacaoDoDolar();
            this.preco = dadosVeiculo.preco() * cotacao;
            this.moeda = "USD";
        } else {
            this.preco = dadosVeiculo.preco();
            this.moeda = dadosVeiculo.moeda();
        }

    }


    private Double obtemCotacaoDolar() {

        ConversorMoedaService service = new ConversorMoedaService();
        this.moeda = "USD";
        return service.obtemCotacaoDoDolar();


    }


    public boolean precisaAtualizarCotacaoDoPreco() {

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


    public Veiculo atualizarInformacoesParciais(Veiculo dadosParciais, Veiculo veiculoDb) {

        Veiculo veiculoAtualizado = new Veiculo();
        ConversorMoedaService service = new ConversorMoedaService();

        //atualiza preco e cotacao se necessario
        if(dadosParciais.precisaAtualizarCotacaoDoPreco()){
            double vlrDolar = service.obtemCotacaoDoDolar();
            System.out.println("LOG: Atualizando preço do veículo com base na cotação do dólar: " + vlrDolar);

            veiculoAtualizado.setPreco(dadosParciais.getPreco() * vlrDolar);
            veiculoAtualizado.setMoeda("USD");
            veiculoAtualizado.setUltimaAtualizacao(LocalDateTime.now());
            System.out.println("LOG: Preço atualizado para: " + veiculoAtualizado.getPreco() + " " + veiculoAtualizado.getMoeda());

        } else {
            System.out.println("LOG: Atualizando preço do veículo sem conversão de moeda.");
            veiculoAtualizado.setPreco(dadosParciais.getPreco());
            veiculoAtualizado.setMoeda(dadosParciais.getMoeda());
        }


        if (dadosParciais.kilometragem != veiculoDb.kilometragem) {
            veiculoAtualizado.setKilometragem(
                    dadosParciais.kilometragem
            );
            System.out.println("LOG: Atualizando kilometragem por parametro recebido.");
        }

        if (dadosParciais.getAtivo()) {
                System.out.println("LOG: Atualizando status para parametro recebido.");
                veiculoAtualizado.setAtivo(true);
        }
        else {
                System.out.println("LOG: Atualizando status para parametro recebido.");
                veiculoAtualizado.setAtivo(false);
        }

        //campos que nao podem ser alterados
        veiculoAtualizado.setId(veiculoDb.getId());
        veiculoAtualizado.setPlaca(veiculoDb.getPlaca());
        veiculoAtualizado.setChassi(veiculoDb.getChassi());
        veiculoAtualizado.setModelo(veiculoDb.getModelo());
        veiculoAtualizado.setMarca(veiculoDb.getMarca());
        veiculoAtualizado.setAno(veiculoDb.getAno());
        veiculoAtualizado.setTipo(veiculoDb.getTipo());

        System.out.println("LOG: Veículo atualizado: " + veiculoAtualizado.toString());
        return veiculoAtualizado;

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
