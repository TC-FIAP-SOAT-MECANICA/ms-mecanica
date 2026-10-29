package br.com.oficina.application.usecase.atendimento;

import br.com.oficina.application.dto.atendimento.ItemServicoInput;
import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;

public interface IncluirServicoNaOrdemUseCase {

    OrdemServicoOutput execute(Integer ordemServicoId, ItemServicoInput input);
}
