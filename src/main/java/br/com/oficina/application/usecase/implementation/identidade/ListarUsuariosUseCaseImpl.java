package br.com.oficina.application.usecase.implementation.identidade;

import br.com.oficina.application.dto.identidade.UsuarioOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.application.usecase.identidade.ListarUsuariosUseCase;
import br.com.oficina.domain.model.identidade.Perfil;

public class ListarUsuariosUseCaseImpl implements ListarUsuariosUseCase {

    private final UsuarioRepository repository;

    public ListarUsuariosUseCaseImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<UsuarioOutput> execute(String perfil, String nome, PageQuery pageQuery) {
        Perfil filtro = perfil == null ? null : Perfil.valueOf(perfil);
        return repository.findAll(filtro, nome, pageQuery).map(UsuarioOutput::from);
    }
}
