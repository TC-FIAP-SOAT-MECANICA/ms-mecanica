package br.com.oficina.application.usecase.implementation.identidade;

import br.com.oficina.application.dto.identidade.CadastrarUsuarioInput;
import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.application.usecase.identidade.CadastrarUsuarioUseCase;
import br.com.oficina.application.usecase.identidade.CriarAdminInicialUseCase;
import br.com.oficina.domain.model.identidade.Perfil;

import java.util.Optional;

public class CriarAdminInicialUseCaseImpl implements CriarAdminInicialUseCase {

    private final UsuarioRepository repository;
    private final CadastrarUsuarioUseCase cadastrarUsuario;

    public CriarAdminInicialUseCaseImpl(UsuarioRepository repository, CadastrarUsuarioUseCase cadastrarUsuario) {
        this.repository = repository;
        this.cadastrarUsuario = cadastrarUsuario;
    }

    @Override
    public Optional<Integer> execute(String nome, String email, String telefone, String senha) {
        if (repository.existsByPerfil(Perfil.ADMIN)) {
            return Optional.empty();
        }
        return Optional.of(cadastrarUsuario.execute(
                new CadastrarUsuarioInput(nome, email, telefone, senha, Perfil.ADMIN.name())).id());
    }
}
