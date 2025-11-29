package br.com.cannamiranda.vehicles_api.veiculo.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

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


    public Veiculo(@Valid DadosVeiculo dadosVeiculo) {
       this.placa = dadosVeiculo.placa();
       this.chassi = dadosVeiculo.chassi();
       this.modelo = dadosVeiculo.modelo();
       this.marca = dadosVeiculo.marca();
       this.ano = dadosVeiculo.ano();
       this.kilometragem = dadosVeiculo.kilometragem();
       this.tipo = dadosVeiculo.tipo();
       this.preco = dadosVeiculo.preco(); //chamar método que valida o valor
       this.ativo = true;
    }


    public void atualizarInformacoes(@Valid DadosAtualizacaoVeiculo dados) {
        if (dados.modelo() != null) {
            this.modelo = dados.modelo();
        }
        if (dados.marca() != null) {
            this.marca = dados.marca();
        }
        if (dados.kilometragem() != 0) {
            this.kilometragem = dados.kilometragem();
        }
    }

    public void setAtivo(boolean b) {
        this.ativo = b;
    }
}
