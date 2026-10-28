package br.com.oficina.atendimento.application.usecase;

import br.com.oficina.atendimento.application.dto.OrdemServicoOutput;
import br.com.oficina.atendimento.application.port.EstoqueGateway;
import br.com.oficina.atendimento.application.port.OrdemServicoRepository;
import br.com.oficina.atendimento.domain.ItemPeca;
import br.com.oficina.atendimento.domain.OrdemServico;
import br.com.oficina.shared.application.port.UnitOfWork;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class RemoverPecaDaOrdemUseCase {

    private final OrdemServicoRepository repository;
    private final EstoqueGateway estoque;
    private final UnitOfWork unitOfWork;

    public RemoverPecaDaOrdemUseCase(OrdemServicoRepository repository, EstoqueGateway estoque,
                                     UnitOfWork unitOfWork) {
        this.repository = repository;
        this.estoque = estoque;
        this.unitOfWork = unitOfWork;
    }

    public OrdemServicoOutput execute(Integer ordemServicoId, Integer itemId) {
        return unitOfWork.execute(() -> {
            OrdemServico ordemServico = repository.findById(ordemServicoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
            ItemPeca removido = ordemServico.removerPeca(itemId);
            estoque.devolver(removido.pecaInsumoId(), removido.quantidade());
            return OrdemServicoOutput.from(repository.save(ordemServico));
        });
    }
}
