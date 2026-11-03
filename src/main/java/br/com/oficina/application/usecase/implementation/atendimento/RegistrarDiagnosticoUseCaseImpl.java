package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.atendimento.RegistrarDiagnosticoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.atendimento.ItemPeca;
import br.com.oficina.domain.model.atendimento.OrdemServico;

public class RegistrarDiagnosticoUseCaseImpl implements RegistrarDiagnosticoUseCase {

    private final OrdemServicoRepository repository;
    private final EstoqueGateway estoque;
    private final UnitOfWork unitOfWork;

    public RegistrarDiagnosticoUseCaseImpl(OrdemServicoRepository repository, EstoqueGateway estoque,
                                           UnitOfWork unitOfWork) {
        this.repository = repository;
        this.estoque = estoque;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public OrdemServicoOutput execute(Integer ordemServicoId, String diagnosticoTecnico) {
        return unitOfWork.execute(() -> {
            OrdemServico ordemServico = repository.findById(ordemServicoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
            ordemServico.registrarDiagnostico(diagnosticoTecnico);
            for (ItemPeca peca : ordemServico.pecas()) {
                estoque.baixar(peca.pecaInsumoId(), peca.quantidade());
            }
            return OrdemServicoOutput.from(repository.save(ordemServico));
        });
    }
}
