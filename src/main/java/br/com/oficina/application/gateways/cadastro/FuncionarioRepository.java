package br.com.oficina.application.gateways.cadastro;

import br.com.oficina.domain.model.cadastro.Funcionario;

import java.util.Optional;

public interface FuncionarioRepository {

    Funcionario save(Funcionario funcionario);

    Optional<Funcionario> findByUsuarioId(Integer usuarioId);
}
