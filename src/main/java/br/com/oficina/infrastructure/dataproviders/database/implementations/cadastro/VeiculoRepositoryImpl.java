package br.com.oficina.infrastructure.dataproviders.database.implementations.cadastro;

import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.gateways.cadastro.VeiculoRepository;
import br.com.oficina.domain.model.cadastro.Placa;
import br.com.oficina.domain.model.cadastro.Veiculo;
import br.com.oficina.infrastructure.dataproviders.database.crypto.IndiceCego;
import br.com.oficina.infrastructure.dataproviders.database.entities.cadastro.VeiculoEntity;
import br.com.oficina.infrastructure.dataproviders.database.mappers.PageMapper;
import br.com.oficina.infrastructure.dataproviders.database.repositories.cadastro.VeiculoJpaRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
class VeiculoRepositoryImpl implements VeiculoRepository {

    private final VeiculoJpaRepository jpaRepository;
    private final IndiceCego indiceCego;

    VeiculoRepositoryImpl(VeiculoJpaRepository jpaRepository, IndiceCego indiceCego) {
        this.jpaRepository = jpaRepository;
        this.indiceCego = indiceCego;
    }

    @Override
    public Veiculo save(Veiculo veiculo) {
        return jpaRepository.saveAndFlush(VeiculoEntity.from(veiculo, hash(veiculo.placa()))).toDomain();
    }

    @Override
    public Optional<Veiculo> findById(Integer id) {
        return jpaRepository.findById(id).map(VeiculoEntity::toDomain);
    }

    @Override
    public boolean existsByPlaca(Placa placa, Integer ignorarId) {
        return ignorarId == null
                ? jpaRepository.existsByPlacaHash(hash(placa))
                : jpaRepository.existsByPlacaHashAndIdNot(hash(placa), ignorarId);
    }

    @Override
    public boolean existsByChassi(String chassi, Integer ignorarId) {
        return ignorarId == null
                ? jpaRepository.existsByChassi(chassi)
                : jpaRepository.existsByChassiAndIdNot(chassi, ignorarId);
    }

    @Override
    public PageResult<Veiculo> findAll(Placa placa, Integer clienteId, PageQuery pageQuery) {
        Specification<VeiculoEntity> filtro = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (placa != null) {
                predicates.add(cb.equal(root.get("placaHash"), hash(placa)));
            }
            if (clienteId != null) {
                predicates.add(cb.equal(root.get("clienteId"), clienteId));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        var pageable = PageMapper.toPageable(pageQuery, Sort.by("id"));
        return PageMapper.toResult(jpaRepository.findAll(filtro, pageable), VeiculoEntity::toDomain);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
        jpaRepository.flush();
    }

    private String hash(Placa placa) {
        return indiceCego.hash(placa.valor());
    }
}
