package br.com.oficina.infrastructure.dataproviders.database.repositories.atendimento;

import br.com.oficina.infrastructure.dataproviders.database.entities.atendimento.OrdemServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrdemServicoJpaRepository extends JpaRepository<OrdemServicoEntity, Integer>,
        JpaSpecificationExecutor<OrdemServicoEntity> {
}
