package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.dto.ClienteOutput;
import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.shared.application.dto.Solicitante;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class ObterClienteUseCase {

    private final ClienteRepository repository;

    public ObterClienteUseCase(ClienteRepository repository) {
        this.repository = repository;
    }

    public ClienteOutput execute(Integer clienteId, Solicitante solicitante) {
        if (!solicitante.podeVerCliente(clienteId)) {
            throw new ResourceNotFoundException("Cliente", clienteId);
        }
        return execute(clienteId);
    }

    public ClienteOutput execute(Integer clienteId) {
        return repository.findById(clienteId)
                .map(ClienteOutput::from)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", clienteId));
    }
}
