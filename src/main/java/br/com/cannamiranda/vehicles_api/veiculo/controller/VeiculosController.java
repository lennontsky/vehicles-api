package br.com.cannamiranda.vehicles_api.veiculo.controller;


import br.com.cannamiranda.vehicles_api.veiculo.model.Veiculo;
import br.com.cannamiranda.vehicles_api.veiculo.model.DadosVeiculo;
import br.com.cannamiranda.vehicles_api.veiculo.repository.VeiculoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/veiculos")

public class VeiculosController {

    @Autowired
    private VeiculoRepository repository;

    @GetMapping
    public Page<Veiculo> listarVeiculos(@PageableDefault(size = 10, sort = {"placa"}) Pageable pageable) {

        return repository.findAll(pageable);
    }

    @PostMapping
    @Transactional
    public void adicionarVeiculo(@RequestBody @Valid DadosVeiculo dadosVeiculo) {
        repository.save(new Veiculo(dadosVeiculo));
    }

}
