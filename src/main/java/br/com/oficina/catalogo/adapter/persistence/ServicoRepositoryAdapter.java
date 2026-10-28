package br.com.oficina.catalogo.adapter.persistence;

import br.com.oficina.catalogo.application.port.ServicoRepository;
import br.com.oficina.catalogo.domain.Servico;
import br.com.oficina.shared.adapter.persistence.PageMapper;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class ServicoRepositoryAdapter implements ServicoRepository {

    private final ServicoJpaRepository jpaRepository;

    ServicoRepositoryAdapter(ServicoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Servico save(Servico servico) {
        return jpaRepository.saveAndFlush(ServicoJpaEntity.from(servico)).toDomain();
    }

    @Override
    public Optional<Servico> findById(Integer id) {
        return jpaRepository.findById(id).map(ServicoJpaEntity::toDomain);
    }

    @Override
    public PageResult<Servico> findAll(String nome, PageQuery pageQuery) {
        var pageable = PageMapper.toPageable(pageQuery, Sort.by("nome", "id"));
        var page = nome == null || nome.isBlank()
                ? jpaRepository.findAll(pageable)
                : jpaRepository.findByNomeContainingIgnoreCase(nome.trim(), pageable);
        return PageMapper.toResult(page, ServicoJpaEntity::toDomain);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
        jpaRepository.flush();
    }
}
