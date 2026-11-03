package br.com.oficina.application.gateways.catalogo;

import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.domain.model.catalogo.Servico;

import java.util.Optional;

public interface ServicoRepository {

    Servico save(Servico servico);

    Optional<Servico> findById(Integer id);

    PageResult<Servico> findAll(String nome, PageQuery pageQuery);

    void deleteById(Integer id);
}
