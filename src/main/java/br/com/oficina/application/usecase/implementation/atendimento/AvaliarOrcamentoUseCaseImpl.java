package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.AvaliarOrcamentoInput;
import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.dto.shared.Solicitante;
import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.atendimento.AvaliarOrcamentoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.atendimento.OrdemServico;
import br.com.oficina.domain.model.atendimento.OrdemServicoPecaInsumo;

import java.time.Clock;

public class AvaliarOrcamentoUseCaseImpl implements AvaliarOrcamentoUseCase {

    private final OrdemServicoRepository repository;
    private final EstoqueGateway estoque;
    private final UnitOfWork unitOfWork;
    private final Clock clock;

    public AvaliarOrcamentoUseCaseImpl(OrdemServicoRepository repository, EstoqueGateway estoque,
                                       UnitOfWork unitOfWork, Clock clock) {
        this.repository = repository;
        this.estoque = estoque;
        this.unitOfWork = unitOfWork;
        this.clock = clock;
    }

    @Override
    public OrdemServicoOutput execute(AvaliarOrcamentoInput input, Solicitante solicitante) {
        return unitOfWork.execute(() -> {
            OrdemServico ordemServico = repository.findById(input.ordemServicoId())
                    .filter(os -> solicitante.podeVerCliente(os.clienteId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", input.ordemServicoId()));
            if (input.aprovado()) {
                ordemServico.aprovarOrcamento(input.agendamentoServico(), clock.instant());
                for (OrdemServicoPecaInsumo peca : ordemServico.pecas()) {
                    estoque.baixar(peca.pecaInsumoId(), peca.quantidade());
                }
            } else {
                ordemServico.rejeitarOrcamento(clock.instant());
            }
            return OrdemServicoOutput.from(repository.save(ordemServico));
        });
    }
}
