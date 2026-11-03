package br.com.oficina.application.usecase.identidade;

import br.com.oficina.application.dto.identidade.TokenOutput;

public interface AutenticarUsuarioUseCase {

    TokenOutput execute(String email, String senha);
}
