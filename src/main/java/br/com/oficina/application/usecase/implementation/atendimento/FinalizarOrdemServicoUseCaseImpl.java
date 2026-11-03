package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.FinalizarOrdemServicoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.atendimento.OrdemServico;

import java.time.Clock;

public class FinalizarOrdemServicoUseCaseImpl implements FinalizarOrdemServicoUseCase {

    private final OrdemServicoRepository repository;
    private final Clock clock;

    public FinalizarOrdemServicoUseCaseImpl(OrdemServicoRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public OrdemServicoOutput execute(Integer ordemServicoId) {
        OrdemServico ordemServico = repository.findById(ordemServicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
        ordemServico.finalizar(clock.instant());
        return OrdemServicoOutput.from(repository.save(ordemServico));
    }
}
