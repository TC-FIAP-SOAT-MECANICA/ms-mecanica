package br.com.oficina.application.usecase.implementation.catalogo;

import br.com.oficina.application.gateways.catalogo.ServicoRepository;
import br.com.oficina.application.usecase.catalogo.RemoverServicoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;

public class RemoverServicoUseCaseImpl implements RemoverServicoUseCase {

    private final ServicoRepository repository;

    public RemoverServicoUseCaseImpl(ServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Integer servicoId) {
        if (repository.findById(servicoId).isEmpty()) {
            throw new ResourceNotFoundException("Serviço", servicoId);
        }
        repository.deleteById(servicoId);
    }
}
