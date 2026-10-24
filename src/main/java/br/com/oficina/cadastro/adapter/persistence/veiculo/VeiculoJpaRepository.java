package br.com.oficina.cadastro.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VeiculoJpaRepository  extends JpaRepository<VeiculoJpaEntity, UUID> {

    List<VeiculoJpaEntity> findByClienteId(UUID clienteId);

}
