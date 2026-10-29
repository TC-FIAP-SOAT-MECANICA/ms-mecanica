package br.com.oficina.application.usecase.catalogo;

import br.com.oficina.application.dto.catalogo.ServicoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;

public interface ListarServicosUseCase {

    PageResult<ServicoOutput> execute(String nome, PageQuery pageQuery);
}
