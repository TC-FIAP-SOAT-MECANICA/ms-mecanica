package br.com.oficina.atendimento.application.usecase;

import br.com.oficina.atendimento.application.dto.OrdemServicoOutput;
import br.com.oficina.atendimento.application.port.OrdemServicoRepository;
import br.com.oficina.atendimento.domain.OrdemServico;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class RemoverServicoDaOrdemUseCase {

    private final OrdemServicoRepository repository;

    public RemoverServicoDaOrdemUseCase(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    public OrdemServicoOutput execute(Integer ordemServicoId, Integer itemId) {
        OrdemServico ordemServico = repository.findById(ordemServicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
        ordemServico.removerServico(itemId);
        return OrdemServicoOutput.from(repository.save(ordemServico));
    }
}
