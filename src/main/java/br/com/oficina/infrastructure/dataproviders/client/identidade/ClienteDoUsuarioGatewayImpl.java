package br.com.oficina.infrastructure.dataproviders.client.identidade;

import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.identidade.ClienteDoUsuarioGateway;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class ClienteDoUsuarioGatewayImpl implements ClienteDoUsuarioGateway {

    private final ClienteRepository clienteRepository;

    ClienteDoUsuarioGatewayImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Optional<Integer> clienteIdDoUsuario(Integer usuarioId) {
        return clienteRepository.findIdByUsuarioId(usuarioId);
    }
}
