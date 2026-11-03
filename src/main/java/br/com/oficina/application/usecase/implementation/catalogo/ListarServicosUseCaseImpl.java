package br.com.oficina.application.usecase.implementation.catalogo;

import br.com.oficina.application.dto.catalogo.ServicoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.gateways.catalogo.ServicoRepository;
import br.com.oficina.application.usecase.catalogo.ListarServicosUseCase;

public class ListarServicosUseCaseImpl implements ListarServicosUseCase {

    private final ServicoRepository repository;

    public ListarServicosUseCaseImpl(ServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<ServicoOutput> execute(String nome, PageQuery pageQuery) {
        return repository.findAll(nome, pageQuery).map(ServicoOutput::from);
    }
}
