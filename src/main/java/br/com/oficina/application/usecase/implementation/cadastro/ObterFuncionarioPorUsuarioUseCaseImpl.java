package br.com.oficina.application.usecase.implementation.cadastro;

import br.com.oficina.application.dto.cadastro.FuncionarioOutput;
import br.com.oficina.application.gateways.cadastro.FuncionarioRepository;
import br.com.oficina.application.usecase.cadastro.ObterFuncionarioPorUsuarioUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;

public class ObterFuncionarioPorUsuarioUseCaseImpl implements ObterFuncionarioPorUsuarioUseCase {

    private final FuncionarioRepository repository;

    public ObterFuncionarioPorUsuarioUseCaseImpl(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public FuncionarioOutput execute(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId)
                .map(FuncionarioOutput::from)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário do usuário", usuarioId));
    }
}
