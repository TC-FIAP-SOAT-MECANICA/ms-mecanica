package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.ItemServicoInput;
import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.gateways.atendimento.CatalogoGateway;
import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.atendimento.IncluirServicoNaOrdemUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.atendimento.OrdemServico;

public class IncluirServicoNaOrdemUseCaseImpl implements IncluirServicoNaOrdemUseCase {

    private final OrdemServicoRepository repository;
    private final ItensOrdemServico itens;
    private final UnitOfWork unitOfWork;

    public IncluirServicoNaOrdemUseCaseImpl(OrdemServicoRepository repository, CatalogoGateway catalogo,
                                        EstoqueGateway estoque, UnitOfWork unitOfWork) {
        this.repository = repository;
        this.itens = new ItensOrdemServico(catalogo, estoque);
        this.unitOfWork = unitOfWork;
    }

    @Override
    public OrdemServicoOutput execute(Integer ordemServicoId, ItemServicoInput input) {
        return unitOfWork.execute(() -> {
            OrdemServico ordemServico = repository.findById(ordemServicoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
            itens.incluirServico(ordemServico, input);
            return OrdemServicoOutput.from(repository.save(ordemServico));
        });
    }
}
