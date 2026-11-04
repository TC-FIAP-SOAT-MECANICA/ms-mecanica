package br.com.oficina.application.usecase.atendimento;

import br.com.oficina.application.dto.atendimento.TempoMedioExecucaoOutput;

import java.time.LocalDate;

public interface CalcularTempoMedioExecucaoUseCase {

    TempoMedioExecucaoOutput execute(LocalDate dataInicio, LocalDate dataFim);
}
