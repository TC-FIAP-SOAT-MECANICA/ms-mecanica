package br.com.oficina.cadastro.adapter.persistence;

import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.cadastro.domain.Cliente;
import br.com.oficina.shared.domain.valueobject.Documento;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter: implementa o port {@link ClienteRepository} com JPA.
 *
 * <p>Recebe e devolve objetos de domínio; a conversão para/de {@link ClienteJpaEntity} acontece só aqui dentro.
 */
@Component
class ClienteRepositoryAdapter implements ClienteRepository {

    private final ClienteJpaRepository jpaRepository;

    ClienteRepositoryAdapter(ClienteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Cliente cliente) {
        jpaRepository.save(ClienteJpaEntity.from(cliente));
    }

    @Override
    public Optional<Cliente> findById(UUID id) {
        return jpaRepository.findById(id).map(ClienteJpaEntity::toDomain);
    }

    @Override
    public boolean existsByDocumento(Documento documento) {
        return jpaRepository.existsByDocumento(documento.valor());
    }
}
