package br.com.oficina.application.usecase.cadastro;

import br.com.oficina.application.dto.cadastro.FuncionarioOutput;

public interface ObterFuncionarioPorUsuarioUseCase {

    FuncionarioOutput execute(Integer usuarioId);
}
