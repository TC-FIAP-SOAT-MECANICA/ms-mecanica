package br.com.oficina.cadastro.adapter.identidade;

import br.com.oficina.cadastro.application.port.UsuarioGateway;
import br.com.oficina.identidade.application.dto.AtualizarContatoInput;
import br.com.oficina.identidade.application.dto.CadastrarUsuarioInput;
import br.com.oficina.identidade.application.dto.UsuarioOutput;
import br.com.oficina.identidade.application.usecase.AtualizarContatoUsuarioUseCase;
import br.com.oficina.identidade.application.usecase.CadastrarUsuarioUseCase;
import br.com.oficina.identidade.application.usecase.RemoverUsuarioUseCase;
import br.com.oficina.identidade.domain.Perfil;
import org.springframework.stereotype.Component;

@Component
class UsuarioGatewayAdapter implements UsuarioGateway {

    private final CadastrarUsuarioUseCase cadastrarUsuario;
    private final AtualizarContatoUsuarioUseCase atualizarContato;
    private final RemoverUsuarioUseCase removerUsuario;

    UsuarioGatewayAdapter(CadastrarUsuarioUseCase cadastrarUsuario, AtualizarContatoUsuarioUseCase atualizarContato,
                          RemoverUsuarioUseCase removerUsuario) {
        this.cadastrarUsuario = cadastrarUsuario;
        this.atualizarContato = atualizarContato;
        this.removerUsuario = removerUsuario;
    }

    @Override
    public UsuarioCriado cadastrarUsuarioCliente(String nome, String email, String telefone, String senha) {
        UsuarioOutput usuario = cadastrarUsuario.execute(
                new CadastrarUsuarioInput(nome, email, telefone, senha, Perfil.CLIENTE.name()));
        return new UsuarioCriado(usuario.id(), usuario.nome(), usuario.email(), usuario.telefone(),
                usuario.criadoEm());
    }

    @Override
    public ContatoUsuario atualizarContato(Integer usuarioId, String nome, String email, String telefone) {
        UsuarioOutput usuario = atualizarContato.execute(new AtualizarContatoInput(usuarioId, nome, email, telefone));
        return new ContatoUsuario(usuario.nome(), usuario.email(), usuario.telefone());
    }

    @Override
    public void removerUsuario(Integer usuarioId) {
        removerUsuario.execute(usuarioId);
    }
}
