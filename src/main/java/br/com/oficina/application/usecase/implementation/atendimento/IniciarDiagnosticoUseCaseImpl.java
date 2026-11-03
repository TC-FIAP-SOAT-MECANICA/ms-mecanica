package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.dto.shared.Solicitante;
import br.com.oficina.application.gateways.atendimento.FuncionarioGateway;
import br.com.oficina.application.gateways.atendimento.FuncionarioGateway.FuncionarioAtendente;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.IniciarDiagnosticoUseCase;
import br.com.oficina.domain.exceptions.BusinessRuleException;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.atendimento.OrdemServico;

public class IniciarDiagnosticoUseCaseImpl implements IniciarDiagnosticoUseCase {

    private final OrdemServicoRepository repository;
    private final FuncionarioGateway funcionarios;

    public IniciarDiagnosticoUseCaseImpl(OrdemServicoRepository repository, FuncionarioGateway funcionarios) {
        this.repository = repository;
        this.funcionarios = funcionarios;
    }

    @Override
    public OrdemServicoOutput execute(Integer ordemServicoId, Solicitante solicitante) {
        FuncionarioAtendente funcionario = funcionarios.funcionarioDoUsuario(solicitante.usuarioId())
                .filter(FuncionarioAtendente::ativo)
                .orElseThrow(() -> new BusinessRuleException("FUNCIONARIO_INATIVO",
                        "Só um funcionário ativo pode assumir o diagnóstico"));
        OrdemServico ordemServico = repository.findById(ordemServicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
        ordemServico.iniciarDiagnostico(funcionario.id());
        return OrdemServicoOutput.from(repository.save(ordemServico));
    }
}
