package br.com.cannamiranda.vehicles_api.veiculo.processor;

import br.com.cannamiranda.vehicles_api.exceptions.PlacaRepetidaException;
import br.com.cannamiranda.vehicles_api.exceptions.VeiculoInexistente;
import br.com.cannamiranda.vehicles_api.veiculo.model.DadosVeiculo;
import br.com.cannamiranda.vehicles_api.veiculo.model.RelatorioMarcas;
import br.com.cannamiranda.vehicles_api.veiculo.model.Veiculo;
import br.com.cannamiranda.vehicles_api.veiculo.repository.VeiculoRepository;
import br.com.cannamiranda.vehicles_api.veiculo.service.ConversorMoedaService;
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

    @Autowired
    ConversorMoedaService conversorMoedaService;

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

            //TODO: Issue #8 Essa implementação ficaria melhor em uma camada de serviço, mas por simplicidade está aqui.
            var veiculo = repository.findByPlaca(dadosVeiculo.placa());
            if (veiculo != null) {
                throw new PlacaRepetidaException(dadosVeiculo.placa());
            }
            else {
                var novoVeiculo = new Veiculo(dadosVeiculo);

                Double valorEmDolar = conversorMoedaService.converterRealParaDolar() * dadosVeiculo.preco();
                novoVeiculo.setPreco(valorEmDolar);

                repository.save(novoVeiculo);
                return ResponseEntity.created(uri).body(novoVeiculo);
            }

    }

    public ResponseEntity<Object> desativarVeiculo(Long id) {

        Optional<Veiculo> veiculo = repository.findById(id);

        if(veiculo.isPresent()){
            var veiculoExistente = veiculo.get();
            veiculoExistente.setAtivo(false);

            repository.save(veiculoExistente);
            System.out.println("LOG: Veículo com ID " + id + " desativado.");

            return ResponseEntity.noContent().build();

        } else {
            throw new VeiculoInexistente("Veículo com ID " + id + " não encontrado.");
        }

    }

        public List<RelatorioMarcas> obterRelatorioVeiculosPorMarca() {

            List<RelatorioMarcas> relatorio = repository.relatorioDeVeiculosPorMarca();

            if (relatorio == null || relatorio.isEmpty()) {
                return null;
            }

            return relatorio;

        }

    public ResponseEntity<Veiculo> atualizarVeiculoParcial(@Valid Veiculo veiculo) {

            var veiculoDB = repository.findById(veiculo.getId());
            if (veiculoDB.isPresent()) {
                Veiculo veiculoAtualizado = veiculo.atualizarInformacoes(veiculoDB.get());
                if (veiculoAtualizado != null) {
                    repository.save(veiculoAtualizado);
                    return ResponseEntity.ok(veiculoAtualizado);
                }
            }

            return ResponseEntity.notFound().build();
    }

}
