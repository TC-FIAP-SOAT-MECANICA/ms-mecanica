package br.com.oficina.application.gateways.cadastro;

import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;

import java.time.Instant;

public interface UsuarioGateway {

    ContatoUsuario cadastrarUsuarioCliente(String nome, String email, String telefone, String senha);

    ContatoUsuario atualizarContato(Integer usuarioId, String nome, String email, String telefone);

    ContatoUsuario buscarContato(Integer usuarioId);

    PageResult<ContatoUsuario> listarContatosDeClientes(String nome, PageQuery pageQuery);

    void removerUsuario(Integer usuarioId);

    record ContatoUsuario(Integer usuarioId, String nome, String email, String telefone, Instant criadoEm) {
    }
}
