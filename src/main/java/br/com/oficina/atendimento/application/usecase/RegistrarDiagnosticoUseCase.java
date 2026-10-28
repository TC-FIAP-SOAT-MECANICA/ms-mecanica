package br.com.oficina.atendimento.application.usecase;

import br.com.oficina.atendimento.application.dto.OrdemServicoOutput;
import br.com.oficina.atendimento.application.port.OrdemServicoRepository;
import br.com.oficina.atendimento.domain.OrdemServico;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class RegistrarDiagnosticoUseCase {

    private final OrdemServicoRepository repository;

    public RegistrarDiagnosticoUseCase(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    public OrdemServicoOutput execute(Integer ordemServicoId, String diagnosticoTecnico) {
        OrdemServico ordemServico = repository.findById(ordemServicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
        ordemServico.registrarDiagnostico(diagnosticoTecnico);
        return OrdemServicoOutput.from(repository.save(ordemServico));
    }
}
