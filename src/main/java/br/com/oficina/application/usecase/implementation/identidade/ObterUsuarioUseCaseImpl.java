package br.com.oficina.application.usecase.implementation.identidade;

import br.com.oficina.application.dto.identidade.UsuarioOutput;
import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.application.usecase.identidade.ObterUsuarioUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;

public class ObterUsuarioUseCaseImpl implements ObterUsuarioUseCase {

    private final UsuarioRepository repository;

    public ObterUsuarioUseCaseImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UsuarioOutput execute(Integer usuarioId) {
        return repository.findById(usuarioId)
                .map(UsuarioOutput::from)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", usuarioId));
    }
}
