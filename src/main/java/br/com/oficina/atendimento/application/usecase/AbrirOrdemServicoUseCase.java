package br.com.oficina.atendimento.application.usecase;

import br.com.oficina.atendimento.application.dto.AbrirOrdemServicoInput;
import br.com.oficina.atendimento.application.dto.ItemPecaInput;
import br.com.oficina.atendimento.application.dto.ItemServicoInput;
import br.com.oficina.atendimento.application.dto.OrdemServicoOutput;
import br.com.oficina.atendimento.application.port.CadastroGateway;
import br.com.oficina.atendimento.application.port.CatalogoGateway;
import br.com.oficina.atendimento.application.port.EstoqueGateway;
import br.com.oficina.atendimento.application.port.OrdemServicoRepository;
import br.com.oficina.atendimento.domain.OrdemServico;
import br.com.oficina.shared.application.port.UnitOfWork;
import br.com.oficina.shared.domain.exception.BusinessRuleException;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

import java.time.Clock;

public class AbrirOrdemServicoUseCase {

    private final OrdemServicoRepository repository;
    private final CadastroGateway cadastro;
    private final ItensOrdemServico itens;
    private final UnitOfWork unitOfWork;
    private final Clock clock;

    public AbrirOrdemServicoUseCase(OrdemServicoRepository repository, CadastroGateway cadastro,
                                    CatalogoGateway catalogo, EstoqueGateway estoque, UnitOfWork unitOfWork,
                                    Clock clock) {
        this.repository = repository;
        this.cadastro = cadastro;
        this.itens = new ItensOrdemServico(catalogo, estoque);
        this.unitOfWork = unitOfWork;
        this.clock = clock;
    }

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
