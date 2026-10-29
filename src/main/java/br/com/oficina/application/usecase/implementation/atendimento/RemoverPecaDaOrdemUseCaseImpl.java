package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.atendimento.RemoverPecaDaOrdemUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.atendimento.ItemPeca;
import br.com.oficina.domain.model.atendimento.OrdemServico;

public class RemoverPecaDaOrdemUseCaseImpl implements RemoverPecaDaOrdemUseCase {

    private final OrdemServicoRepository repository;
    private final EstoqueGateway estoque;
    private final UnitOfWork unitOfWork;

    public RemoverPecaDaOrdemUseCaseImpl(OrdemServicoRepository repository, EstoqueGateway estoque,
                                     UnitOfWork unitOfWork) {
        this.repository = repository;
        this.estoque = estoque;
        this.unitOfWork = unitOfWork;
    }

    @Override
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
