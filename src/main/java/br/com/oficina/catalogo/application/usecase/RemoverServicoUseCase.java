package br.com.oficina.catalogo.application.usecase;

import br.com.oficina.catalogo.application.port.ServicoRepository;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class RemoverServicoUseCase {

    private final ServicoRepository repository;

    public RemoverServicoUseCase(ServicoRepository repository) {
        this.repository = repository;
    }

    public void execute(Integer servicoId) {
        if (repository.findById(servicoId).isEmpty()) {
            throw new ResourceNotFoundException("Serviço", servicoId);
        }
        repository.deleteById(servicoId);
    }
}
