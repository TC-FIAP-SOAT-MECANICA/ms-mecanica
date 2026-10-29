package br.com.oficina.application.usecase.implementation.identidade;

import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.application.usecase.identidade.RemoverUsuarioUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;

public class RemoverUsuarioUseCaseImpl implements RemoverUsuarioUseCase {

    private final UsuarioRepository repository;

    public RemoverUsuarioUseCaseImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Integer usuarioId) {
        if (repository.findById(usuarioId).isEmpty()) {
            throw new ResourceNotFoundException("Usuário", usuarioId);
        }
        repository.deleteById(usuarioId);
    }
}
