package br.com.oficina.catalogo.application.port;

import br.com.oficina.catalogo.domain.Servico;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;

import java.util.Optional;

public interface ServicoRepository {

    Servico save(Servico servico);

    Optional<Servico> findById(Integer id);

    PageResult<Servico> findAll(String nome, PageQuery pageQuery);

    void deleteById(Integer id);
}
