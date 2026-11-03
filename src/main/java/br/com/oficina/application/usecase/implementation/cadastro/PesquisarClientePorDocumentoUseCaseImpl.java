package br.com.oficina.application.usecase.implementation.cadastro;

import br.com.oficina.application.dto.cadastro.ClienteOutput;
import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway;
import br.com.oficina.application.usecase.cadastro.PesquisarClientePorDocumentoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.valueobject.Documento;

public class PesquisarClientePorDocumentoUseCaseImpl implements PesquisarClientePorDocumentoUseCase {

    private final ClienteRepository repository;
    private final UsuarioGateway usuarioGateway;

    public PesquisarClientePorDocumentoUseCaseImpl(ClienteRepository repository, UsuarioGateway usuarioGateway) {
        this.repository = repository;
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public ClienteOutput execute(String documento) {
        Documento valor = new Documento(documento);
        return repository.findByDocumento(valor)
                .map(cliente -> ClienteOutput.from(cliente, usuarioGateway.buscarContato(cliente.usuarioId())))
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", valor));
    }
}
