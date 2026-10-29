package br.com.oficina.infrastructure.dataproviders.client.cadastro;

import br.com.oficina.application.dto.identidade.AtualizarContatoInput;
import br.com.oficina.application.dto.identidade.CadastrarUsuarioInput;
import br.com.oficina.application.dto.identidade.UsuarioOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway;
import br.com.oficina.application.usecase.identidade.AtualizarContatoUsuarioUseCase;
import br.com.oficina.application.usecase.identidade.CadastrarUsuarioUseCase;
import br.com.oficina.application.usecase.identidade.ListarUsuariosUseCase;
import br.com.oficina.application.usecase.identidade.ObterUsuarioUseCase;
import br.com.oficina.application.usecase.identidade.RemoverUsuarioUseCase;
import org.springframework.stereotype.Component;

@Component
class UsuarioGatewayImpl implements UsuarioGateway {

    private static final String PERFIL_CLIENTE = "CLIENTE";

    private final CadastrarUsuarioUseCase cadastrarUsuario;
    private final AtualizarContatoUsuarioUseCase atualizarContato;
    private final ObterUsuarioUseCase obterUsuario;
    private final ListarUsuariosUseCase listarUsuarios;
    private final RemoverUsuarioUseCase removerUsuario;

    UsuarioGatewayImpl(CadastrarUsuarioUseCase cadastrarUsuario, AtualizarContatoUsuarioUseCase atualizarContato,
                       ObterUsuarioUseCase obterUsuario, ListarUsuariosUseCase listarUsuarios,
                       RemoverUsuarioUseCase removerUsuario) {
        this.cadastrarUsuario = cadastrarUsuario;
        this.atualizarContato = atualizarContato;
        this.obterUsuario = obterUsuario;
        this.listarUsuarios = listarUsuarios;
        this.removerUsuario = removerUsuario;
    }

    @Override
    public ContatoUsuario cadastrarUsuarioCliente(String nome, String email, String telefone, String senha) {
        return toContato(cadastrarUsuario.execute(
                new CadastrarUsuarioInput(nome, email, telefone, senha, PERFIL_CLIENTE)));
    }

    @Override
    public ContatoUsuario atualizarContato(Integer usuarioId, String nome, String email, String telefone) {
        return toContato(atualizarContato.execute(new AtualizarContatoInput(usuarioId, nome, email, telefone)));
    }

    @Override
    public ContatoUsuario buscarContato(Integer usuarioId) {
        return toContato(obterUsuario.execute(usuarioId));
    }

    @Override
    public PageResult<ContatoUsuario> listarContatosDeClientes(String nome, PageQuery pageQuery) {
        return listarUsuarios.execute(PERFIL_CLIENTE, nome, pageQuery).map(UsuarioGatewayImpl::toContato);
    }

    @Override
    public void removerUsuario(Integer usuarioId) {
        removerUsuario.execute(usuarioId);
    }

    private static ContatoUsuario toContato(UsuarioOutput usuario) {
        return new ContatoUsuario(usuario.id(), usuario.nome(), usuario.email(), usuario.telefone(),
                usuario.criadoEm());
    }
}
