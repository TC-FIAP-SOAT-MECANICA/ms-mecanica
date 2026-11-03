package br.com.oficina.application.usecase.cadastro;

import br.com.oficina.application.dto.cadastro.ClienteOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;

public interface ListarClientesUseCase {

    PageResult<ClienteOutput> execute(String nome, PageQuery pageQuery);
}
