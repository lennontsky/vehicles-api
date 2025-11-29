package br.com.cannamiranda.vehicles_api.veiculo.controller;


import br.com.cannamiranda.vehicles_api.veiculo.processor.VeiculoProcessor;
import br.com.cannamiranda.vehicles_api.veiculo.model.Veiculo;
import br.com.cannamiranda.vehicles_api.veiculo.model.DadosVeiculo;
import br.com.cannamiranda.vehicles_api.veiculo.repository.VeiculoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/veiculos")

public class VeiculosController {

    @Autowired
    private VeiculoProcessor processor;
    @Autowired
    private VeiculoRepository repository;

//    @GetMapping
//    @Operation(summary = "Lista todos os veiculos", description = "Retorna json com todos os veículos cadastrados no sistema.")
//    public ResponseEntity<Page<List<Veiculo>>> consultaTodosVeiculos(@PageableDefault(size = 10) Pageable pageable,
//                                                                     @RequestParam(required = false) String valorMinimo,
//                                                                     @RequestParam(required = false) String valorMaximo,
//                                                                     @RequestParam(required = false) String cor,
//                                                                     @RequestParam(required = false) String marca,
//                                                                     @RequestParam(required = false) String ano) {
//        return processor.buscarVeiculos(pageable);
//    }

    @GetMapping
    @Operation(summary = "Lista todos os veiculos", description = "Retorna json com todos os veículos cadastrados no sistema.")
    public ResponseEntity<Page<List<Veiculo>>> consultaTodosVeiculos(@PageableDefault(size = 10) Pageable pageable,
                                                                     @RequestParam(required = false) String valorMinimo,
                                                                     @RequestParam(required = false) String valorMaximo,
                                                                     @RequestParam(required = false) String cor,
                                                                     @RequestParam(required = false) String marca,
                                                                     @RequestParam(required = false) String ano) {

        Map<String, String> filtros = new java.util.HashMap<>();
        if (valorMinimo != null) filtros.put("valorMinimo", valorMinimo);
        if (valorMaximo != null) filtros.put("valorMaximo", valorMaximo);
        if (cor != null) filtros.put("cor", cor);
        if (marca != null) filtros.put("marca", marca);
        if (ano != null) filtros.put("ano", ano);

        System.out.println("LOG: Filtros recebidos - " + filtros.toString());

        return processor.buscarVeiculos(pageable, filtros);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Detalha um veículo pelo ID", description = "Retorna os detalhes de um veículo específico com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    public ResponseEntity<Veiculo> detalhesVeiculo(@PathVariable Long id) {
        return processor.buscarVeiculoPorId(id);
    }

    @GetMapping("/relatorios/por-marca")
    @Operation(summary = "Relatório de veículos por marca", description = "Gera um relatório que agrupa os veículos cadastrados por marca.")
    public ResponseEntity<List<Veiculo>> relatorioVeiculosPorMarca(@PageableDefault(size = 10) Pageable pageable) {
       return processor.obterRelatorioVeiculosPorMarca();
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Adiciona novo veiculo", description = "Recebe os dados de um novo veículo e o adiciona ao sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veículo cadastrado com sucesso")
    })
    public ResponseEntity<Veiculo> adicionarVeiculo(@RequestBody @Valid DadosVeiculo dadosVeiculo, UriComponentsBuilder uriComponentsBuilder) {
        var uri = uriComponentsBuilder.path("/veiculos/{id}").buildAndExpand(dadosVeiculo.placa()).toUri();
        return processor.adicionarVeiculo(dadosVeiculo, uri);
    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Veiculo> atualizarVeiculoCompletamente(@RequestBody @Valid Veiculo veiculo) {
        return processor.atualizarVeiculoCompleto(veiculo);
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarVeiculo(@PathVariable Long id) {
        return processor.desativarVeiculo(id);
    }


}
