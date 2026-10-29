package br.com.oficina.application.usecase.catalogo;

import br.com.oficina.application.dto.catalogo.ServicoInput;
import br.com.oficina.application.dto.catalogo.ServicoOutput;

public interface AtualizarServicoUseCase {

    ServicoOutput execute(Integer servicoId, ServicoInput input);
}
