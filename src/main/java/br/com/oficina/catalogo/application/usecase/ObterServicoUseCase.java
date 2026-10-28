package br.com.oficina.catalogo.application.usecase;

import br.com.oficina.catalogo.application.dto.ServicoOutput;
import br.com.oficina.catalogo.application.port.ServicoRepository;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class ObterServicoUseCase {

    private final ServicoRepository repository;

    public ObterServicoUseCase(ServicoRepository repository) {
        this.repository = repository;
    }

    public ServicoOutput execute(Integer servicoId) {
        return repository.findById(servicoId)
                .map(ServicoOutput::from)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço", servicoId));
    }
}
