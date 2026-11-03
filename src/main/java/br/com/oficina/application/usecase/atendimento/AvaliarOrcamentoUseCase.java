package br.com.oficina.application.usecase.atendimento;

import br.com.oficina.application.dto.atendimento.AvaliarOrcamentoInput;
import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.dto.shared.Solicitante;

public interface AvaliarOrcamentoUseCase {

    OrdemServicoOutput execute(AvaliarOrcamentoInput input, Solicitante solicitante);
}
