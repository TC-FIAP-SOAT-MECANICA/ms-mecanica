package br.com.oficina.identidade.application.usecase;

import br.com.oficina.identidade.application.port.UsuarioRepository;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class RemoverUsuarioUseCase {

    private final UsuarioRepository repository;

    public RemoverUsuarioUseCase(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void execute(Integer usuarioId) {
        if (repository.findById(usuarioId).isEmpty()) {
            throw new ResourceNotFoundException("Usuário", usuarioId);
        }
        repository.deleteById(usuarioId);
    }
}
