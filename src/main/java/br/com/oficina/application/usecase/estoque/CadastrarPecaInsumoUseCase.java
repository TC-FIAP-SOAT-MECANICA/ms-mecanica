package br.com.oficina.application.usecase.estoque;

import br.com.oficina.application.dto.estoque.PecaInsumoInput;
import br.com.oficina.application.dto.estoque.PecaInsumoOutput;

public interface CadastrarPecaInsumoUseCase {

    PecaInsumoOutput execute(PecaInsumoInput input);
}
