package br.com.oficina.infrastructure.dataproviders.database.implementations.estoque;

import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.domain.model.estoque.PecaInsumo;
import br.com.oficina.infrastructure.dataproviders.database.entities.estoque.PecaInsumoEntity;
import br.com.oficina.infrastructure.dataproviders.database.mappers.PageMapper;
import br.com.oficina.infrastructure.dataproviders.database.repositories.estoque.PecaInsumoJpaRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
class PecaInsumoRepositoryImpl implements PecaInsumoRepository {

    private final PecaInsumoJpaRepository jpaRepository;

    PecaInsumoRepositoryImpl(PecaInsumoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PecaInsumo save(PecaInsumo pecaInsumo) {
        return jpaRepository.saveAndFlush(PecaInsumoEntity.from(pecaInsumo)).toDomain();
    }

    @Override
    public Optional<PecaInsumo> findById(Integer id) {
        return jpaRepository.findById(id).map(PecaInsumoEntity::toDomain);
    }

    @Override
    public Optional<PecaInsumo> findByIdForUpdate(Integer id) {
        return jpaRepository.findByIdForUpdate(id).map(PecaInsumoEntity::toDomain);
    }

    @Override
    public boolean existsBySku(String sku, Integer ignorarId) {
        return ignorarId == null ? jpaRepository.existsBySku(sku) : jpaRepository.existsBySkuAndIdNot(sku, ignorarId);
    }

    @Override
    public PageResult<PecaInsumo> findAll(String nome, boolean somenteAbaixoDoMinimo, PageQuery pageQuery) {
        Specification<PecaInsumoEntity> filtro = (root, query, cb) -> {
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
        return PageMapper.toResult(jpaRepository.findAll(filtro, pageable), PecaInsumoEntity::toDomain);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
        jpaRepository.flush();
    }
}
