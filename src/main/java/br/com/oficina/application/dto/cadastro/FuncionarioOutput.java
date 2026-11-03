package br.com.oficina.application.dto.cadastro;

import br.com.oficina.domain.model.cadastro.Funcionario;

public record FuncionarioOutput(Integer id, Integer usuarioId, boolean ativo) {

    public static FuncionarioOutput from(Funcionario funcionario) {
        return new FuncionarioOutput(funcionario.id(), funcionario.usuarioId(), funcionario.ativo());
    }
}
