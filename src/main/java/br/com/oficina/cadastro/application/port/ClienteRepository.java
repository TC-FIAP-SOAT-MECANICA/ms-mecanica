package br.com.oficina.cadastro.application.port;

import br.com.oficina.cadastro.domain.Cliente;
import br.com.oficina.shared.domain.valueobject.Documento;

import java.util.Optional;
import java.util.UUID;

/**
 * Port: o que os casos de uso de cliente precisam do banco. Implementado por
 * {@code cadastro.adapter.persistence.ClienteRepositoryAdapter}.
 *
 * <p>Os métodos falam a língua do domínio ({@link Cliente}, {@link Documento}), nunca de JPA.
 */
public interface ClienteRepository {

    void save(Cliente cliente);

    Optional<Cliente> findById(UUID id);

    boolean existsByDocumento(Documento documento);
}
