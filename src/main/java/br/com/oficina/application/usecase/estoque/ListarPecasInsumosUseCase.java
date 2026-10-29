package br.com.oficina.application.usecase.estoque;

import br.com.oficina.application.dto.estoque.PecaInsumoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;

public interface ListarPecasInsumosUseCase {

    PageResult<PecaInsumoOutput> execute(String nome, Boolean abaixoDoMinimo, PageQuery pageQuery);
}
