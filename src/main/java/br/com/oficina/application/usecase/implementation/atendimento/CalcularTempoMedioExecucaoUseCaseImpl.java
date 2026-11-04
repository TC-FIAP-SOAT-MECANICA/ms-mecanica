package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.TempoMedioExecucaoOutput;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.CalcularTempoMedioExecucaoUseCase;
import br.com.oficina.domain.exceptions.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CalcularTempoMedioExecucaoUseCaseImpl implements CalcularTempoMedioExecucaoUseCase {

    private static final int MAXIMO_DIAS = 366;
    private static final ZoneId FUSO_OFICINA = ZoneId.of("America/Sao_Paulo");
    private static final BigDecimal SEGUNDOS_POR_MINUTO = BigDecimal.valueOf(60);

    private final OrdemServicoRepository repository;

    public CalcularTempoMedioExecucaoUseCaseImpl(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public TempoMedioExecucaoOutput execute(LocalDate dataInicio, LocalDate dataFim) {
        validarPeriodo(dataInicio, dataFim);
        List<Duration> tempos = repository.temposDeExecucao(dataInicio.atStartOfDay(FUSO_OFICINA).toInstant(),
                dataFim.plusDays(1).atStartOfDay(FUSO_OFICINA).toInstant());
        return new TempoMedioExecucaoOutput(dataInicio, dataFim, tempos.size(), mediaEmMinutos(tempos));
    }

    private static void validarPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new ValidationException("Informe dataInicio e dataFim");
        }
        if (dataFim.isBefore(dataInicio)) {
            throw new ValidationException("dataFim não pode ser anterior a dataInicio");
        }
        if (ChronoUnit.DAYS.between(dataInicio, dataFim) + 1 > MAXIMO_DIAS) {
            throw new ValidationException("O período pode ter no máximo " + MAXIMO_DIAS + " dias");
        }
    }

    private static BigDecimal mediaEmMinutos(List<Duration> tempos) {
        if (tempos.isEmpty()) {
            return null;
        }
        long totalSegundos = tempos.stream().mapToLong(Duration::toSeconds).sum();
        return BigDecimal.valueOf(totalSegundos)
                .divide(SEGUNDOS_POR_MINUTO.multiply(BigDecimal.valueOf(tempos.size())), 1, RoundingMode.HALF_UP);
    }
}
