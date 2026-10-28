package br.com.oficina.atendimento.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface OrdemServicoJpaRepository extends JpaRepository<OrdemServicoJpaEntity, Integer>,
        JpaSpecificationExecutor<OrdemServicoJpaEntity> {
}
