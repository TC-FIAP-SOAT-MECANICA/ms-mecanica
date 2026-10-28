package br.com.oficina.estoque.application.usecase;

import br.com.oficina.estoque.application.dto.PecaInsumoOutput;
import br.com.oficina.estoque.application.port.PecaInsumoRepository;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;

public class ListarPecasInsumosUseCase {

    private final PecaInsumoRepository repository;

    public ListarPecasInsumosUseCase(PecaInsumoRepository repository) {
        this.repository = repository;
    }

    public PageResult<PecaInsumoOutput> execute(String nome, Boolean abaixoDoMinimo, PageQuery pageQuery) {
        return repository.findAll(nome, Boolean.TRUE.equals(abaixoDoMinimo), pageQuery).map(PecaInsumoOutput::from);
    }
}
