package br.com.oficina.application.usecase.estoque;

import br.com.oficina.application.dto.estoque.PecaInsumoOutput;

import java.math.BigDecimal;

public interface MovimentarEstoqueUseCase {

    PecaInsumoOutput execute(Integer pecaId, String tipo, BigDecimal quantidade);
}
