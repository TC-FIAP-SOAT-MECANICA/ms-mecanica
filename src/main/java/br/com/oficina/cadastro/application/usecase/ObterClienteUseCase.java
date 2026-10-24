package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.dto.ClienteOutput;
import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

import java.util.UUID;

/** Caso de uso: detalhar cliente ({@code GET /clientes/{clienteId}}). */
public class ObterClienteUseCase {

    private final ClienteRepository repository;

    public ObterClienteUseCase(ClienteRepository repository) {
        this.repository = repository;
    }

    public ClienteOutput execute(UUID clienteId) {
        return repository.findById(clienteId)
                .map(ClienteOutput::from)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", clienteId));
    }
}
