package br.com.oficina.estoque.adapter.persistence;

import br.com.oficina.estoque.application.port.PecaInsumoRepository;
import br.com.oficina.estoque.domain.PecaInsumo;
import br.com.oficina.shared.adapter.persistence.PageMapper;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
class PecaInsumoRepositoryAdapter implements PecaInsumoRepository {

    private final PecaInsumoJpaRepository jpaRepository;

    PecaInsumoRepositoryAdapter(PecaInsumoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PecaInsumo save(PecaInsumo pecaInsumo) {
        return jpaRepository.saveAndFlush(PecaInsumoJpaEntity.from(pecaInsumo)).toDomain();
    }

    @Override
    public Optional<PecaInsumo> findById(Integer id) {
        return jpaRepository.findById(id).map(PecaInsumoJpaEntity::toDomain);
    }

    @Override
    public Optional<PecaInsumo> findByIdForUpdate(Integer id) {
        return jpaRepository.findByIdForUpdate(id).map(PecaInsumoJpaEntity::toDomain);
    }

    @Override
    public boolean existsBySku(String sku, Integer ignorarId) {
        return ignorarId == null ? jpaRepository.existsBySku(sku) : jpaRepository.existsBySkuAndIdNot(sku, ignorarId);
    }

    @Override
    public PageResult<PecaInsumo> findAll(String nome, boolean somenteAbaixoDoMinimo, PageQuery pageQuery) {
        Specification<PecaInsumoJpaEntity> filtro = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.trim().toLowerCase() + "%"));
            }
            if (somenteAbaixoDoMinimo) {
                predicates.add(cb.lessThan(root.get("quantidadeEstoque"), root.get("estoqueMinimo")));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        var pageable = PageMapper.toPageable(pageQuery, Sort.by("nome", "id"));
        return PageMapper.toResult(jpaRepository.findAll(filtro, pageable), PecaInsumoJpaEntity::toDomain);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
        jpaRepository.flush();
    }
}
