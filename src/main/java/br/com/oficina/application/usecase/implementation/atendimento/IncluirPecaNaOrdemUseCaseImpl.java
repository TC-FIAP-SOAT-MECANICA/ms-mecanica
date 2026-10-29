package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.ItemPecaInput;
import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.gateways.atendimento.CatalogoGateway;
import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.atendimento.IncluirPecaNaOrdemUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.atendimento.OrdemServico;

public class IncluirPecaNaOrdemUseCaseImpl implements IncluirPecaNaOrdemUseCase {

    private final OrdemServicoRepository repository;
    private final ItensOrdemServico itens;
    private final UnitOfWork unitOfWork;

    public IncluirPecaNaOrdemUseCaseImpl(OrdemServicoRepository repository, CatalogoGateway catalogo,
                                     EstoqueGateway estoque, UnitOfWork unitOfWork) {
        this.repository = repository;
        this.itens = new ItensOrdemServico(catalogo, estoque);
        this.unitOfWork = unitOfWork;
    }

    @Override
    public OrdemServicoOutput execute(Integer ordemServicoId, ItemPecaInput input) {
        return unitOfWork.execute(() -> {
            OrdemServico ordemServico = repository.findById(ordemServicoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
            itens.incluirPeca(ordemServico, input);
            return OrdemServicoOutput.from(repository.save(ordemServico));
        });
    }
}
