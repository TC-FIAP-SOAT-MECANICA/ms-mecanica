package br.com.oficina.application.usecase.atendimento;

import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;

public interface RegistrarDiagnosticoUseCase {

    OrdemServicoOutput execute(Integer ordemServicoId, String diagnosticoTecnico);
}
