package br.com.oficina.atendimento.adapter.persistence;

import br.com.oficina.atendimento.application.port.OrdemServicoRepository;
import br.com.oficina.atendimento.domain.OrdemServico;
import br.com.oficina.atendimento.domain.StatusOrdemServico;
import br.com.oficina.shared.adapter.persistence.PageMapper;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
class OrdemServicoRepositoryAdapter implements OrdemServicoRepository {

    private final OrdemServicoJpaRepository jpaRepository;

    OrdemServicoRepositoryAdapter(OrdemServicoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public OrdemServico save(OrdemServico ordemServico) {
        return jpaRepository.saveAndFlush(OrdemServicoJpaEntity.from(ordemServico)).toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdemServico> findById(Integer id) {
        return jpaRepository.findById(id).map(OrdemServicoJpaEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<OrdemServico> findAll(List<StatusOrdemServico> status, Integer clienteId, Integer veiculoId,
                                            PageQuery pageQuery) {
        Specification<OrdemServicoJpaEntity> filtro = (root, query, cb) -> {
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
        return PageMapper.toResult(jpaRepository.findAll(filtro, pageable), OrdemServicoJpaEntity::toDomain);
    }
}
