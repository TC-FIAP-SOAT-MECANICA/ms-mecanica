package br.com.oficina.atendimento.application.usecase;

import br.com.oficina.atendimento.application.dto.OrdemServicoOutput;
import br.com.oficina.atendimento.application.port.OrdemServicoRepository;
import br.com.oficina.atendimento.domain.OrdemServico;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

import java.time.Clock;

public class FinalizarOrdemServicoUseCase {

    private final OrdemServicoRepository repository;
    private final Clock clock;

    public FinalizarOrdemServicoUseCase(OrdemServicoRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public OrdemServicoOutput execute(Integer ordemServicoId) {
        OrdemServico ordemServico = repository.findById(ordemServicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
        ordemServico.finalizar(clock.instant());
        return OrdemServicoOutput.from(repository.save(ordemServico));
    }
}
