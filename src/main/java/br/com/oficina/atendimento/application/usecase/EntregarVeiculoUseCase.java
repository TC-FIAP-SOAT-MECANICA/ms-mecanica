package br.com.oficina.atendimento.application.usecase;

import br.com.oficina.atendimento.application.dto.OrdemServicoOutput;
import br.com.oficina.atendimento.application.port.OrdemServicoRepository;
import br.com.oficina.atendimento.domain.OrdemServico;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class EntregarVeiculoUseCase {

    private final OrdemServicoRepository repository;

    public EntregarVeiculoUseCase(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    public OrdemServicoOutput execute(Integer ordemServicoId) {
        OrdemServico ordemServico = repository.findById(ordemServicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
        ordemServico.entregar();
        return OrdemServicoOutput.from(repository.save(ordemServico));
    }
}
