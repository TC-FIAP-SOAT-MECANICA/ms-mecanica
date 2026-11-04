package br.com.oficina.entrypoints.controllers.metricas;

import br.com.oficina.application.usecase.atendimento.CalcularTempoMedioExecucaoUseCase;
import br.com.oficina.contract.api.MetricasApi;
import br.com.oficina.contract.model.MetricaTempoExecucaoResponseDto;
import br.com.oficina.entrypoints.controllers.presenter.metricas.MetricasPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class MetricasController implements MetricasApi {

    private final CalcularTempoMedioExecucaoUseCase calcularTempoMedioExecucao;

    public MetricasController(CalcularTempoMedioExecucaoUseCase calcularTempoMedioExecucao) {
        this.calcularTempoMedioExecucao = calcularTempoMedioExecucao;
    }

    @Override
    public ResponseEntity<MetricaTempoExecucaoResponseDto> calcularTempoMedioExecucao(LocalDate dataInicio,
                                                                                      LocalDate dataFim) {
        return ResponseEntity.ok(MetricasPresenter.toResponse(calcularTempoMedioExecucao.execute(dataInicio, dataFim)));
    }
}
