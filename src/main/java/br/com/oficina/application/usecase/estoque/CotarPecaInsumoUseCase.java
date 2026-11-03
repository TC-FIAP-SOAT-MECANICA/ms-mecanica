package br.com.oficina.application.usecase.estoque;

import br.com.oficina.application.dto.estoque.PecaInsumoOutput;

import java.math.BigDecimal;

public interface CotarPecaInsumoUseCase {

    PecaInsumoOutput execute(Integer pecaId, BigDecimal quantidade);
}
