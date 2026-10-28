package br.com.oficina.cadastro.adapter.persistence.cliente;

import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.cadastro.domain.Cliente;
import br.com.oficina.shared.adapter.persistence.PageMapper;
import br.com.oficina.shared.adapter.persistence.crypto.IndiceCego;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
import br.com.oficina.shared.domain.valueobject.Documento;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class ClienteRepositoryAdapter implements ClienteRepository {

    private final ClienteJpaRepository jpaRepository;
    private final IndiceCego indiceCego;

    ClienteRepositoryAdapter(ClienteJpaRepository jpaRepository, IndiceCego indiceCego) {
        this.jpaRepository = jpaRepository;
        this.indiceCego = indiceCego;
    }

    @Override
    public Cliente save(Cliente cliente) {
        ClienteJpaEntity salvo = jpaRepository.saveAndFlush(
                ClienteJpaEntity.from(cliente, indiceCego.hash(cliente.documento().valor())));
        return cliente.comId(salvo.id());
    }

    @Override
    public Optional<Cliente> findById(Integer id) {
        return jpaRepository.findWithContatoById(id).map(ClienteJpaEntity::toDomain);
    }

    @Override
    public Optional<Cliente> findByDocumento(Documento documento) {
        return jpaRepository.findByCpfCnpjHash(indiceCego.hash(documento.valor())).map(ClienteJpaEntity::toDomain);
    }

    @Override
    public Optional<Integer> findIdByUsuarioId(Integer usuarioId) {
        return jpaRepository.findIdByUsuarioId(usuarioId);
    }

    @Override
    public boolean existsByDocumento(Documento documento) {
        return jpaRepository.existsByCpfCnpjHash(indiceCego.hash(documento.valor()));
    }

    @Override
    public boolean existsById(Integer id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public PageResult<Cliente> findAll(String nome, PageQuery pageQuery) {
        Specification<ClienteJpaEntity> filtro = (root, query, cb) -> nome == null || nome.isBlank()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("contato").get("nome")), "%" + nome.trim().toLowerCase() + "%");
        var pageable = PageMapper.toPageable(pageQuery, Sort.by("contato.nome", "id"));
        return PageMapper.toResult(jpaRepository.findAll(filtro, pageable), ClienteJpaEntity::toDomain);
    }
}
