package br.com.oficina.identidade.application.usecase;

import br.com.oficina.identidade.application.dto.CadastrarUsuarioInput;
import br.com.oficina.identidade.application.port.UsuarioRepository;
import br.com.oficina.identidade.domain.Perfil;

import java.util.Optional;

public class CriarAdminInicialUseCase {

    private final UsuarioRepository repository;
    private final CadastrarUsuarioUseCase cadastrarUsuario;

    public CriarAdminInicialUseCase(UsuarioRepository repository, CadastrarUsuarioUseCase cadastrarUsuario) {
        this.repository = repository;
        this.cadastrarUsuario = cadastrarUsuario;
    }

    public Optional<Integer> execute(String nome, String email, String telefone, String senha) {
        if (repository.existsByPerfil(Perfil.ADMIN)) {
            return Optional.empty();
        }
        return Optional.of(cadastrarUsuario.execute(
                new CadastrarUsuarioInput(nome, email, telefone, senha, Perfil.ADMIN.name())).id());
    }
}
