package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.dto.shared.Solicitante;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.ObterOrdemServicoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;

public class ObterOrdemServicoUseCaseImpl implements ObterOrdemServicoUseCase {

    private final OrdemServicoRepository repository;

    public ObterOrdemServicoUseCaseImpl(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public OrdemServicoOutput execute(Integer ordemServicoId, Solicitante solicitante) {
        return repository.findById(ordemServicoId)
                .filter(os -> solicitante.podeVerCliente(os.clienteId()))
                .map(OrdemServicoOutput::from)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço", ordemServicoId));
    }
}
