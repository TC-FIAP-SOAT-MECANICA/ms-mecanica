package br.com.oficina.application.usecase.atendimento;

import br.com.oficina.application.dto.atendimento.AbrirOrdemServicoInput;
import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;

public interface AbrirOrdemServicoUseCase {

    OrdemServicoOutput execute(AbrirOrdemServicoInput input);
}
