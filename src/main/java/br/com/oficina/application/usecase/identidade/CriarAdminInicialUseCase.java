package br.com.oficina.application.usecase.identidade;

import java.util.Optional;

public interface CriarAdminInicialUseCase {

    Optional<Integer> execute(String nome, String email, String telefone, String senha);
}
