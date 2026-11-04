package br.com.oficina.infrastructure.dataproviders.database.repositories.atendimento;

import br.com.oficina.domain.model.atendimento.StatusAprovacao;
import br.com.oficina.domain.model.atendimento.StatusOrdemServico;
import br.com.oficina.infrastructure.dataproviders.database.entities.atendimento.OrdemServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface OrdemServicoJpaRepository extends JpaRepository<OrdemServicoEntity, Integer>,
        JpaSpecificationExecutor<OrdemServicoEntity> {

    @Query("""
            select o.dataAbertura as dataAbertura, o.dataConclusao as dataConclusao
            from OrdemServicoEntity o
            where o.status in :status
              and o.statusAprovacaoRejeicao = :aprovacao
              and o.dataConclusao >= :desde and o.dataConclusao < :ate
            """)
    List<PeriodoExecucao> findPeriodosDeExecucao(@Param("status") Collection<StatusOrdemServico> status,
                                                 @Param("aprovacao") StatusAprovacao aprovacao,
                                                 @Param("desde") Instant desde, @Param("ate") Instant ate);

    interface PeriodoExecucao {

        Instant getDataAbertura();

        Instant getDataConclusao();
    }
}
