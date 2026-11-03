package br.com.oficina.application.usecase.identidade;

import br.com.oficina.application.dto.identidade.UsuarioOutput;

public interface ObterUsuarioUseCase {

    UsuarioOutput execute(Integer usuarioId);
}
