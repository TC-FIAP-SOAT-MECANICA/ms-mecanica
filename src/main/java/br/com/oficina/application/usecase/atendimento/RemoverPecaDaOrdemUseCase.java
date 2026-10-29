package br.com.oficina.application.usecase.atendimento;

import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;

public interface RemoverPecaDaOrdemUseCase {

    OrdemServicoOutput execute(Integer ordemServicoId, Integer itemId);
}
