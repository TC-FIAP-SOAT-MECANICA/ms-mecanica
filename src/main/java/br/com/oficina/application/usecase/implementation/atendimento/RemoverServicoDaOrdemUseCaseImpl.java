package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.RemoverServicoDaOrdemUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.atendimento.OrdemServico;

public class RemoverServicoDaOrdemUseCaseImpl implements RemoverServicoDaOrdemUseCase {

    private final OrdemServicoRepository repository;

    public RemoverServicoDaOrdemUseCaseImpl(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public OrdemServicoOutput execute(Integer ordemServicoId, Integer itemId) {
        OrdemServico ordemServico = repository.findById(ordemServicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
        ordemServico.removerServico(itemId);
        return OrdemServicoOutput.from(repository.save(ordemServico));
    }
}
