package br.com.oficina.infrastructure.dataproviders.database.implementations.atendimento;

import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.domain.model.atendimento.OrdemServico;
import br.com.oficina.domain.model.atendimento.StatusOrdemServico;
import br.com.oficina.infrastructure.dataproviders.database.entities.atendimento.OrdemServicoEntity;
import br.com.oficina.infrastructure.dataproviders.database.mappers.PageMapper;
import br.com.oficina.infrastructure.dataproviders.database.repositories.atendimento.OrdemServicoJpaRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
class OrdemServicoRepositoryImpl implements OrdemServicoRepository {

    private final OrdemServicoJpaRepository jpaRepository;

    OrdemServicoRepositoryImpl(OrdemServicoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public OrdemServico save(OrdemServico ordemServico) {
        return jpaRepository.saveAndFlush(OrdemServicoEntity.from(ordemServico)).toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdemServico> findById(Integer id) {
        return jpaRepository.findById(id).map(OrdemServicoEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<OrdemServico> findAll(List<StatusOrdemServico> status, Integer clienteId, Integer veiculoId,
                                            PageQuery pageQuery) {
        Specification<OrdemServicoEntity> filtro = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!status.isEmpty()) {
                predicates.add(root.get("status").in(status));
            }
            if (clienteId != null) {
                predicates.add(cb.equal(root.get("clienteId"), clienteId));
            }
            if (veiculoId != null) {
                predicates.add(cb.equal(root.get("veiculoId"), veiculoId));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        var pageable = PageMapper.toPageable(pageQuery, Sort.by(Sort.Direction.DESC, "dataAbertura", "id"));
        return PageMapper.toResult(jpaRepository.findAll(filtro, pageable), OrdemServicoEntity::toDomain);
    }
}
