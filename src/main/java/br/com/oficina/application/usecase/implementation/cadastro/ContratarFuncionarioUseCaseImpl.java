package br.com.oficina.application.usecase.implementation.cadastro;

import br.com.oficina.application.dto.cadastro.FuncionarioOutput;
import br.com.oficina.application.gateways.cadastro.FuncionarioRepository;
import br.com.oficina.application.usecase.cadastro.ContratarFuncionarioUseCase;
import br.com.oficina.domain.model.cadastro.Funcionario;

public class ContratarFuncionarioUseCaseImpl implements ContratarFuncionarioUseCase {

    private final FuncionarioRepository repository;

    public ContratarFuncionarioUseCaseImpl(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public FuncionarioOutput execute(Integer usuarioId) {
        Funcionario funcionario = repository.findByUsuarioId(usuarioId)
                .orElseGet(() -> repository.save(Funcionario.contratar(usuarioId)));
        return FuncionarioOutput.from(funcionario);
    }
}
