package br.com.oficina.cadastro.application.port;

import br.com.oficina.cadastro.domain.Cliente;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
import br.com.oficina.shared.domain.valueobject.Documento;

import java.util.Optional;

public interface ClienteRepository {

    Cliente save(Cliente cliente);

    Optional<Cliente> findById(Integer id);

    Optional<Cliente> findByDocumento(Documento documento);

    Optional<Integer> findIdByUsuarioId(Integer usuarioId);

    boolean existsByDocumento(Documento documento);

    boolean existsById(Integer id);

    PageResult<Cliente> findAll(String nome, PageQuery pageQuery);
}
