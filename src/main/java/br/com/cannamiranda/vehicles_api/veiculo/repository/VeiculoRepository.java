package br.com.cannamiranda.vehicles_api.veiculo.repository;

import br.com.cannamiranda.vehicles_api.veiculo.model.Veiculo;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    Object findByPlaca(@NotBlank String placa);

    List<Veiculo> findAllByOrderByMarcaAsc();

    Page<List<Veiculo>> findByPrecoBetween(Double precoInicial, Double precoFinal, Pageable pageable);

}