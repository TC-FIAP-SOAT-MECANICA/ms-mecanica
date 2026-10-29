package br.com.oficina.application.usecase.implementation.estoque;

import br.com.oficina.application.dto.estoque.PecaInsumoInput;
import br.com.oficina.application.dto.estoque.PecaInsumoOutput;
import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.application.usecase.estoque.CadastrarPecaInsumoUseCase;
import br.com.oficina.domain.exceptions.ConflictException;
import br.com.oficina.domain.model.estoque.PecaInsumo;

public class CadastrarPecaInsumoUseCaseImpl implements CadastrarPecaInsumoUseCase {

    private final PecaInsumoRepository repository;

    public CadastrarPecaInsumoUseCaseImpl(PecaInsumoRepository repository) {
        this.repository = repository;
    }

    @Override
    public PecaInsumoOutput execute(PecaInsumoInput input) {
        PecaInsumo peca = PecaInsumo.cadastrar(input.toDados());
        if (repository.existsBySku(peca.sku(), null)) {
            throw new ConflictException("SKU_JA_CADASTRADO", "Já existe peça/insumo com este SKU");
        }
        return PecaInsumoOutput.from(repository.save(peca));
    }
}
