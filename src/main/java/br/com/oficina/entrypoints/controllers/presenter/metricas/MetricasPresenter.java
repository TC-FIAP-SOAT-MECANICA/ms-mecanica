package br.com.oficina.entrypoints.controllers.presenter.metricas;

import br.com.oficina.application.dto.atendimento.TempoMedioExecucaoOutput;
import br.com.oficina.contract.model.MetricaTempoExecucaoResponseDto;

public final class MetricasPresenter {

    private MetricasPresenter() {
    }

    public static MetricaTempoExecucaoResponseDto toResponse(TempoMedioExecucaoOutput metrica) {
        return new MetricaTempoExecucaoResponseDto()
                .periodoInicio(metrica.periodoInicio())
                .periodoFim(metrica.periodoFim())
                .totalOrdensConsideradas(metrica.totalOrdensConsideradas())
                .tempoMedioExecucaoMinutos(metrica.tempoMedioExecucaoMinutos());
    }
}
