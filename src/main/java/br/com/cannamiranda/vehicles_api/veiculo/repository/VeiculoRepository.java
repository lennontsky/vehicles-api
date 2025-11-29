package br.com.cannamiranda.vehicles_api.veiculo.repository;

import br.com.cannamiranda.vehicles_api.veiculo.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
}