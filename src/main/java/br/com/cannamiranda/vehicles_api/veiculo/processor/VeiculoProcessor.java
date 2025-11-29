package br.com.cannamiranda.vehicles_api.veiculo.processor;

import br.com.cannamiranda.vehicles_api.exceptions.PlacaRepetidaException;
import br.com.cannamiranda.vehicles_api.exceptions.VeiculoInexistente;
import br.com.cannamiranda.vehicles_api.veiculo.model.DadosVeiculo;
import br.com.cannamiranda.vehicles_api.veiculo.model.Veiculo;
import br.com.cannamiranda.vehicles_api.veiculo.repository.VeiculoRepository;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class VeiculoProcessor {

    @Autowired
    VeiculoRepository repository;

    public ResponseEntity<Veiculo> buscarVeiculoPorId(Long id) {
        var veiculoOptional = repository.findById(id);
        if (veiculoOptional.isPresent()) {
            return ResponseEntity.ok(veiculoOptional.get());
        } else {
            String msg = "Veículo com ID " + id + " não encontrado.";
            System.out.println("LOG: " + msg);
           throw new VeiculoInexistente(msg);
        }
    }


    public ResponseEntity<Page<List<Veiculo>>> buscarVeiculos(@Parameter(hidden = true) Pageable pageable, Map<String, String> filtros) {

        System.out.println("LOG: Filtros recebidos - " + filtros);

        if( filtros != null && filtros.containsKey("valorMinimo") && filtros.containsKey("valorMaximo")) {
            Double valorMinimo = Double.valueOf(filtros.get("valorMinimo"));
            Double valorMaximo = Double.valueOf(filtros.get("valorMaximo"));
            var page = repository.findByPrecoBetween(valorMinimo, valorMaximo, pageable);
            return ResponseEntity.ok(page);
        }



        else{
            var page = repository.findAll(pageable).map(veiculo -> List.of(veiculo));
            return ResponseEntity.ok(page);
        }


    }

    public ResponseEntity<Veiculo> atualizarVeiculoCompleto(@Valid Veiculo veiculo) {

        var veiculoOptional = repository.findById(veiculo.getId());
        if (veiculoOptional.isPresent()) {
            repository.save(veiculo);
            return ResponseEntity.ok(veiculo);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Veiculo> adicionarVeiculo(@Valid DadosVeiculo dadosVeiculo, URI uri) {

 //       try{
            var veiculo = repository.findByPlaca(dadosVeiculo.placa());
            if (veiculo != null) {
                throw new PlacaRepetidaException(dadosVeiculo.placa());
            }
            else {
                var novoVeiculo = new Veiculo(dadosVeiculo);
                repository.save(novoVeiculo);
                return ResponseEntity.created(uri).body(novoVeiculo);
            }
//        } //catch (PlacaRepetidaException ex) {
//            return badRequest().build();
//        }
    }

    public ResponseEntity<Object> desativarVeiculo(Long id) {

        Optional<Veiculo> veiculo = repository.findById(id);

        if(veiculo.isPresent()){
            var veiculoExistente = veiculo.get();
            veiculoExistente.setAtivo(false);
            repository.save(veiculoExistente);

            return ResponseEntity.noContent().build();

        } else {
            throw new VeiculoInexistente("Veículo com ID " + id + " não encontrado.");
        }

    }

    public ResponseEntity<List<Veiculo>> obterRelatorioVeiculosPorMarca() {
        var relatorio = repository.findAllByOrderByMarcaAsc();
        if (relatorio == null) {
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.ok().body(relatorio);
        }
    }

//    public ResponseEntity<Page<List<Veiculo>>> buscarVeiculosPorFaixaDePreco(Double precoInicial, Double precoFinal, Pageable pageable) {
//        var veiculos = repository.findByPrecoBetween(precoInicial, precoFinal, pageable);
//        if (veiculos == null || veiculos.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok().body(veiculos);
//        }
//    }


//    public ResponseEntity<List<Veiculo>> buscarVeiculos(java.util.Map<String, String> filtros) {
//        List<Veiculo> veiculos;
//        if (filtros == null || filtros.isEmpty()) {
//            veiculos = repository.findAll();
//        } else {
//            // Assumindo que o repositório expõe um método que aceita os filtros recebidos
//            veiculos = repository.findByFilters(filtros);
//
//            repository.findAll((Sort) filtros);
//        }
//        return ResponseEntity.ok(veiculos);
//    }


}
