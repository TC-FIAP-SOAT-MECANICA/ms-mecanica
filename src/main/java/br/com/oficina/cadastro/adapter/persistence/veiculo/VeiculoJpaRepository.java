package br.com.oficina.cadastro.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VeiculoJpaRepository  extends JpaRepository<VeiculoJpaEntity, UUID> {

    VeiculoJpaEntity obterVeiculoPorCliente(UUID clientId);

}
