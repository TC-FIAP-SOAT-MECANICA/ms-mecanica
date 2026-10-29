package br.com.oficina.application.usecase.implementation.estoque;

import br.com.oficina.application.dto.estoque.PecaInsumoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.application.usecase.estoque.ListarPecasInsumosUseCase;

public class ListarPecasInsumosUseCaseImpl implements ListarPecasInsumosUseCase {

    private final PecaInsumoRepository repository;

    public ListarPecasInsumosUseCaseImpl(PecaInsumoRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<PecaInsumoOutput> execute(String nome, Boolean abaixoDoMinimo, PageQuery pageQuery) {
        return repository.findAll(nome, Boolean.TRUE.equals(abaixoDoMinimo), pageQuery).map(PecaInsumoOutput::from);
    }
}
