package br.com.oficina.application.usecase.estoque;

import br.com.oficina.application.dto.estoque.PecaInsumoOutput;

public interface ObterPecaInsumoUseCase {

    PecaInsumoOutput execute(Integer pecaId);
}
