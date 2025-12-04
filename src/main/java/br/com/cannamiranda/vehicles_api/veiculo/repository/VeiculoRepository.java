package br.com.cannamiranda.vehicles_api.veiculo.repository;

import br.com.cannamiranda.vehicles_api.veiculo.model.Veiculo;
import br.com.cannamiranda.vehicles_api.veiculo.processor.RelatorioMarcas;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    Object findByPlaca(@NotBlank String placa);

    List<Veiculo> findAllByOrderByMarcaAsc();

    Page<List<Veiculo>> findByPrecoBetween(Double precoInicial, Double precoFinal, Pageable pageable);


    @Query(value ="SELECT COALESCE(marca, 'SEM_MARCA') AS marca, COUNT(*) AS total FROM veiculos GROUP BY marca ORDER BY total DESC", nativeQuery = true)
    List<RelatorioMarcas> relatorioDeVeiculosPorMarca();


}