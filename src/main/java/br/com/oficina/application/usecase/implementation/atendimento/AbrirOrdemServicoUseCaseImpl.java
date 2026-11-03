package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.AbrirOrdemServicoInput;
import br.com.oficina.application.dto.atendimento.ItemPecaInput;
import br.com.oficina.application.dto.atendimento.ItemServicoInput;
import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.gateways.atendimento.CadastroGateway;
import br.com.oficina.application.gateways.atendimento.CatalogoGateway;
import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.atendimento.AbrirOrdemServicoUseCase;
import br.com.oficina.domain.exceptions.BusinessRuleException;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.atendimento.OrdemServico;

import java.time.Clock;

public class AbrirOrdemServicoUseCaseImpl implements AbrirOrdemServicoUseCase {

    private final OrdemServicoRepository repository;
    private final CadastroGateway cadastro;
    private final ItensOrdemServico itens;
    private final UnitOfWork unitOfWork;
    private final Clock clock;

    public AbrirOrdemServicoUseCaseImpl(OrdemServicoRepository repository, CadastroGateway cadastro,
                                    CatalogoGateway catalogo, EstoqueGateway estoque, UnitOfWork unitOfWork,
                                    Clock clock) {
        this.repository = repository;
        this.cadastro = cadastro;
        this.itens = new ItensOrdemServico(catalogo, estoque);
        this.unitOfWork = unitOfWork;
        this.clock = clock;
    }

    @Override
    public OrdemServicoOutput execute(AbrirOrdemServicoInput input) {
        return unitOfWork.execute(() -> {
            validarClienteEVeiculo(input.clienteId(), input.veiculoId());
            OrdemServico ordemServico = OrdemServico.abrir(input.clienteId(), input.veiculoId(),
                    input.descricaoProblema(), clock.instant());
            for (ItemServicoInput servico : input.servicos()) {
                itens.incluirServico(ordemServico, servico);
            }
            for (ItemPecaInput peca : input.pecas()) {
                itens.incluirPeca(ordemServico, peca);
            }
            return OrdemServicoOutput.from(repository.save(ordemServico));
        });
    }

    private void validarClienteEVeiculo(Integer clienteId, Integer veiculoId) {
        if (!cadastro.clienteExiste(clienteId)) {
            throw new ResourceNotFoundException("Cliente", clienteId);
        }
        Integer dono = cadastro.clienteDoVeiculo(veiculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo", veiculoId));
        if (!dono.equals(clienteId)) {
            throw new BusinessRuleException("VEICULO_NAO_PERTENCE_AO_CLIENTE", "O veículo não pertence ao cliente informado");
        }
    }
}
