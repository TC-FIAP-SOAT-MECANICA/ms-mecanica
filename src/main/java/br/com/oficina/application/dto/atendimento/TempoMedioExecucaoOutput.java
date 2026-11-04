package br.com.oficina.application.dto.atendimento;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * {@code tempoMedioExecucaoMinutos} é nulo quando nenhuma OS foi concluída no período.
 */
public record TempoMedioExecucaoOutput(LocalDate periodoInicio, LocalDate periodoFim, int totalOrdensConsideradas,
                                       BigDecimal tempoMedioExecucaoMinutos) {
}
