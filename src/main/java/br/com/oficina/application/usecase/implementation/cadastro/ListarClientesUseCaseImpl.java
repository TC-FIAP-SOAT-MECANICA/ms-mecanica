package br.com.oficina.application.usecase.implementation.cadastro;

import br.com.oficina.application.dto.cadastro.ClienteOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway.ContatoUsuario;
import br.com.oficina.application.usecase.cadastro.ListarClientesUseCase;
import br.com.oficina.domain.model.cadastro.Cliente;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListarClientesUseCaseImpl implements ListarClientesUseCase {

    private final ClienteRepository repository;
    private final UsuarioGateway usuarioGateway;

    public ListarClientesUseCaseImpl(ClienteRepository repository, UsuarioGateway usuarioGateway) {
        this.repository = repository;
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public PageResult<ClienteOutput> execute(String nome, PageQuery pageQuery) {
        PageResult<ContatoUsuario> contatos = usuarioGateway.listarContatosDeClientes(nome, pageQuery);
        Map<Integer, Cliente> clientesPorUsuario = repository
                .findByUsuarioIds(contatos.content().stream().map(ContatoUsuario::usuarioId).toList())
                .stream()
                .collect(Collectors.toMap(Cliente::usuarioId, Function.identity()));
        return new PageResult<>(contatos.content().stream()
                .filter(contato -> clientesPorUsuario.containsKey(contato.usuarioId()))
                .map(contato -> ClienteOutput.from(clientesPorUsuario.get(contato.usuarioId()), contato))
                .toList(), contatos.page(), contatos.size(), contatos.totalElements(), contatos.totalPages());
    }
}
