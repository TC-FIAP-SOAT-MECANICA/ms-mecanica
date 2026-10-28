package br.com.oficina.identidade.application.usecase;

import br.com.oficina.identidade.application.dto.UsuarioOutput;
import br.com.oficina.identidade.application.port.UsuarioRepository;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class ObterUsuarioUseCase {

    private final UsuarioRepository repository;

    public ObterUsuarioUseCase(UsuarioRepository repository) {
        this.repository = repository;
    }

    public UsuarioOutput execute(Integer usuarioId) {
        return repository.findById(usuarioId)
                .map(UsuarioOutput::from)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", usuarioId));
    }
}
