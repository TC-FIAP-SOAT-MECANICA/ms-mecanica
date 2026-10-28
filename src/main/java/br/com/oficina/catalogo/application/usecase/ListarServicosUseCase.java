package br.com.oficina.catalogo.application.usecase;

import br.com.oficina.catalogo.application.dto.ServicoOutput;
import br.com.oficina.catalogo.application.port.ServicoRepository;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;

public class ListarServicosUseCase {

    private final ServicoRepository repository;

    public ListarServicosUseCase(ServicoRepository repository) {
        this.repository = repository;
    }

    public PageResult<ServicoOutput> execute(String nome, PageQuery pageQuery) {
        return repository.findAll(nome, pageQuery).map(ServicoOutput::from);
    }
}
