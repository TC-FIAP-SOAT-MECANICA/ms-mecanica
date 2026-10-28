package br.com.oficina.atendimento.application.usecase;

import br.com.oficina.atendimento.application.dto.OrdemServicoOutput;
import br.com.oficina.atendimento.application.port.OrdemServicoRepository;
import br.com.oficina.shared.application.dto.Solicitante;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class ObterOrdemServicoUseCase {

    private final OrdemServicoRepository repository;

    public ObterOrdemServicoUseCase(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    public OrdemServicoOutput execute(Integer ordemServicoId, Solicitante solicitante) {
        return repository.findById(ordemServicoId)
                .filter(os -> solicitante.podeVerCliente(os.clienteId()))
                .map(OrdemServicoOutput::from)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
    }
}
