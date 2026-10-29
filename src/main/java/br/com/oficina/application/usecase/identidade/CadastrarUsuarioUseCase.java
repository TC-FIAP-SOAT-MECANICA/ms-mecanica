package br.com.oficina.application.usecase.identidade;

import br.com.oficina.application.dto.identidade.CadastrarUsuarioInput;
import br.com.oficina.application.dto.identidade.UsuarioOutput;

public interface CadastrarUsuarioUseCase {

    UsuarioOutput execute(CadastrarUsuarioInput input);
}
