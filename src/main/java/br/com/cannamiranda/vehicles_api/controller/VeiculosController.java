package br.com.cannamiranda.vehicles_api.controller;


import br.com.cannamiranda.vehicles_api.model.Veiculo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veiculos")

public class VeiculosController {

    @GetMapping
    public String listarTodosVeiculos() {
        //TODO implementar a lógica para listar todos os veículos
        return "Lista de veículos";
    }

    @PostMapping
    public Veiculo adicionarVeiculo(@RequestBody Veiculo veiculo) {
        //TODO implementar a lógica para adicionar um novo veículo
        System.out.println(veiculo);
        return veiculo;
    }

}
