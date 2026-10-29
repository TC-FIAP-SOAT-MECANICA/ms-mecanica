package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.EntregarVeiculoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.atendimento.OrdemServico;

public class EntregarVeiculoUseCaseImpl implements EntregarVeiculoUseCase {

    private final OrdemServicoRepository repository;

    public EntregarVeiculoUseCaseImpl(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public OrdemServicoOutput execute(Integer ordemServicoId) {
        OrdemServico ordemServico = repository.findById(ordemServicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
        ordemServico.entregar();
        return OrdemServicoOutput.from(repository.save(ordemServico));
    }
}
