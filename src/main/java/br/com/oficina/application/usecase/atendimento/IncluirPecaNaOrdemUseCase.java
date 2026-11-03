package br.com.oficina.application.usecase.atendimento;

import br.com.oficina.application.dto.atendimento.ItemPecaInput;
import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;

public interface IncluirPecaNaOrdemUseCase {

    OrdemServicoOutput execute(Integer ordemServicoId, ItemPecaInput input);
}
