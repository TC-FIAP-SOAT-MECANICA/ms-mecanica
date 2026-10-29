package br.com.oficina.application.usecase.identidade;

import br.com.oficina.application.dto.identidade.AtualizarContatoInput;
import br.com.oficina.application.dto.identidade.UsuarioOutput;

public interface AtualizarContatoUsuarioUseCase {

    UsuarioOutput execute(AtualizarContatoInput input);
}
