package br.com.oficina.application.gateways.cadastro;

import br.com.oficina.domain.model.cadastro.Cliente;
import br.com.oficina.domain.valueobject.Documento;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    Cliente save(Cliente cliente);

    Optional<Cliente> findById(Integer id);

    Optional<Cliente> findByDocumento(Documento documento);

    Optional<Integer> findIdByUsuarioId(Integer usuarioId);

    List<Cliente> findByUsuarioIds(Collection<Integer> usuarioIds);

    boolean existsByDocumento(Documento documento);

    boolean existsById(Integer id);
}
