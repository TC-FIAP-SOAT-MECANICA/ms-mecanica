package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.dto.ClienteOutput;
import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;

public class ListarClientesUseCase {

    private final ClienteRepository repository;

    public ListarClientesUseCase(ClienteRepository repository) {
        this.repository = repository;
    }

    public PageResult<ClienteOutput> execute(String nome, PageQuery pageQuery) {
        return repository.findAll(nome, pageQuery).map(ClienteOutput::from);
    }
}
