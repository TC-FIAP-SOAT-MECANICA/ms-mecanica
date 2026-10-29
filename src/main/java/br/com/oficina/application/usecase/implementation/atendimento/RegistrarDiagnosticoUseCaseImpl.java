package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.RegistrarDiagnosticoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.atendimento.OrdemServico;

public class RegistrarDiagnosticoUseCaseImpl implements RegistrarDiagnosticoUseCase {

    private final OrdemServicoRepository repository;

    public RegistrarDiagnosticoUseCaseImpl(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public OrdemServicoOutput execute(Integer ordemServicoId, String diagnosticoTecnico) {
        OrdemServico ordemServico = repository.findById(ordemServicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
        ordemServico.registrarDiagnostico(diagnosticoTecnico);
        return OrdemServicoOutput.from(repository.save(ordemServico));
    }
}
