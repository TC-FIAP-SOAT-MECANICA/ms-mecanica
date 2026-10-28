package br.com.oficina.atendimento.application.usecase;

import br.com.oficina.atendimento.application.dto.ItemServicoInput;
import br.com.oficina.atendimento.application.dto.OrdemServicoOutput;
import br.com.oficina.atendimento.application.port.CatalogoGateway;
import br.com.oficina.atendimento.application.port.EstoqueGateway;
import br.com.oficina.atendimento.application.port.OrdemServicoRepository;
import br.com.oficina.atendimento.domain.OrdemServico;
import br.com.oficina.shared.application.port.UnitOfWork;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class IncluirServicoNaOrdemUseCase {

    private final OrdemServicoRepository repository;
    private final ItensOrdemServico itens;
    private final UnitOfWork unitOfWork;

    public IncluirServicoNaOrdemUseCase(OrdemServicoRepository repository, CatalogoGateway catalogo,
                                        EstoqueGateway estoque, UnitOfWork unitOfWork) {
        this.repository = repository;
        this.itens = new ItensOrdemServico(catalogo, estoque);
        this.unitOfWork = unitOfWork;
    }

    public OrdemServicoOutput execute(Integer ordemServicoId, ItemServicoInput input) {
        return unitOfWork.execute(() -> {
            OrdemServico ordemServico = repository.findById(ordemServicoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
            itens.incluirServico(ordemServico, input);
            return OrdemServicoOutput.from(repository.save(ordemServico));
        });
    }
}
