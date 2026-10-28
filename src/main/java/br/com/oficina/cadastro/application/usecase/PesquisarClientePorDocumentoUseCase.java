package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.dto.ClienteOutput;
import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;
import br.com.oficina.shared.domain.valueobject.Documento;

public class PesquisarClientePorDocumentoUseCase {

    private final ClienteRepository repository;

    public PesquisarClientePorDocumentoUseCase(ClienteRepository repository) {
        this.repository = repository;
    }

    public ClienteOutput execute(String documento) {
        Documento valor = new Documento(documento);
        return repository.findByDocumento(valor)
                .map(ClienteOutput::from)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", valor));
    }
}
