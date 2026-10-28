package br.com.oficina.cadastro.adapter.persistence.veiculo;

import br.com.oficina.cadastro.application.port.VeiculoRepository;
import br.com.oficina.cadastro.domain.Placa;
import br.com.oficina.cadastro.domain.Veiculo;
import br.com.oficina.shared.adapter.persistence.PageMapper;
import br.com.oficina.shared.adapter.persistence.crypto.IndiceCego;
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
class VeiculoRepositoryAdapter implements VeiculoRepository {

    private final VeiculoJpaRepository jpaRepository;
    private final IndiceCego indiceCego;

    VeiculoRepositoryAdapter(VeiculoJpaRepository jpaRepository, IndiceCego indiceCego) {
        this.jpaRepository = jpaRepository;
        this.indiceCego = indiceCego;
    }

    @Override
    public Veiculo save(Veiculo veiculo) {
        return jpaRepository.saveAndFlush(VeiculoJpaEntity.from(veiculo, hash(veiculo.placa()))).toDomain();
    }

    @Override
    public Optional<Veiculo> findById(Integer id) {
        return jpaRepository.findById(id).map(VeiculoJpaEntity::toDomain);
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
        Specification<VeiculoJpaEntity> filtro = (root, query, cb) -> {
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
        return PageMapper.toResult(jpaRepository.findAll(filtro, pageable), VeiculoJpaEntity::toDomain);
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
