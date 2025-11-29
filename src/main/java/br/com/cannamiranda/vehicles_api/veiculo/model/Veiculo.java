package br.com.cannamiranda.vehicles_api.veiculo.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "veiculos")
@Entity(name = "Veiculo")
@Getter
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


    public Veiculo(@Valid DadosVeiculo dadosVeiculo) {
       this.placa = dadosVeiculo.placa();
       this.chassi = dadosVeiculo.chassi();
       this.modelo = dadosVeiculo.modelo();
       this.marca = dadosVeiculo.marca();
       this.ano = dadosVeiculo.ano();
       this.kilometragem = dadosVeiculo.kilometragem();
       this.tipo = dadosVeiculo.tipo();

    }
}
