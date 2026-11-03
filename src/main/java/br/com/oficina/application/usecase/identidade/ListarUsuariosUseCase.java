package br.com.oficina.application.usecase.identidade;

import br.com.oficina.application.dto.identidade.UsuarioOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;

public interface ListarUsuariosUseCase {

    PageResult<UsuarioOutput> execute(String perfil, String nome, PageQuery pageQuery);
}
