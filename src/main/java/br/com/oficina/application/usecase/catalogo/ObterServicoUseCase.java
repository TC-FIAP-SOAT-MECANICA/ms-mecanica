package br.com.oficina.application.usecase.catalogo;

import br.com.oficina.application.dto.catalogo.ServicoOutput;

public interface ObterServicoUseCase {

    ServicoOutput execute(Integer servicoId);
}
