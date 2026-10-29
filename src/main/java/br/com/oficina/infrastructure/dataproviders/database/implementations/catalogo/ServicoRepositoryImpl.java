package br.com.oficina.infrastructure.dataproviders.database.implementations.catalogo;

import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.gateways.catalogo.ServicoRepository;
import br.com.oficina.domain.model.catalogo.Servico;
import br.com.oficina.infrastructure.dataproviders.database.entities.catalogo.ServicoEntity;
import br.com.oficina.infrastructure.dataproviders.database.mappers.PageMapper;
import br.com.oficina.infrastructure.dataproviders.database.repositories.catalogo.ServicoJpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class ServicoRepositoryImpl implements ServicoRepository {

    private final ServicoJpaRepository jpaRepository;

    ServicoRepositoryImpl(ServicoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Servico save(Servico servico) {
        return jpaRepository.saveAndFlush(ServicoEntity.from(servico)).toDomain();
    }

    @Override
    public Optional<Servico> findById(Integer id) {
        return jpaRepository.findById(id).map(ServicoEntity::toDomain);
    }

    @Override
    public PageResult<Servico> findAll(String nome, PageQuery pageQuery) {
        var pageable = PageMapper.toPageable(pageQuery, Sort.by("nome", "id"));
        var page = nome == null || nome.isBlank()
                ? jpaRepository.findAll(pageable)
                : jpaRepository.findByNomeContainingIgnoreCase(nome.trim(), pageable);
        return PageMapper.toResult(page, ServicoEntity::toDomain);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
        jpaRepository.flush();
    }
}
