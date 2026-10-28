package br.com.oficina.identidade.application.usecase;

import br.com.oficina.identidade.application.dto.UsuarioOutput;
import br.com.oficina.identidade.application.port.UsuarioRepository;
import br.com.oficina.identidade.domain.Perfil;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;

public class ListarUsuariosUseCase {

    private final UsuarioRepository repository;

    public ListarUsuariosUseCase(UsuarioRepository repository) {
        this.repository = repository;
    }

    public PageResult<UsuarioOutput> execute(String perfil, PageQuery pageQuery) {
        Perfil filtro = perfil == null ? null : Perfil.valueOf(perfil);
        return repository.findAll(filtro, pageQuery).map(UsuarioOutput::from);
    }
}
