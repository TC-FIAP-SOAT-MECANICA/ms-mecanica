package br.com.oficina.application.usecase.atendimento;

import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.dto.shared.Solicitante;

public interface IniciarDiagnosticoUseCase {

    OrdemServicoOutput execute(Integer ordemServicoId, Solicitante solicitante);
}
