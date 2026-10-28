package br.com.oficina.cadastro.adapter.identidade;

import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.identidade.application.port.ClienteDoUsuarioPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class ClienteDoUsuarioAdapter implements ClienteDoUsuarioPort {

    private final ClienteRepository clienteRepository;

    ClienteDoUsuarioAdapter(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Optional<Integer> clienteIdDoUsuario(Integer usuarioId) {
        return clienteRepository.findIdByUsuarioId(usuarioId);
    }
}
