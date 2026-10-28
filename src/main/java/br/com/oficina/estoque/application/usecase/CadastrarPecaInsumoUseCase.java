package br.com.oficina.estoque.application.usecase;

import br.com.oficina.estoque.application.dto.PecaInsumoInput;
import br.com.oficina.estoque.application.dto.PecaInsumoOutput;
import br.com.oficina.estoque.application.port.PecaInsumoRepository;
import br.com.oficina.estoque.domain.PecaInsumo;
import br.com.oficina.shared.domain.exception.ConflictException;

public class CadastrarPecaInsumoUseCase {

    private final PecaInsumoRepository repository;

    public CadastrarPecaInsumoUseCase(PecaInsumoRepository repository) {
        this.repository = repository;
    }

    public PecaInsumoOutput execute(PecaInsumoInput input) {
        PecaInsumo peca = PecaInsumo.cadastrar(input.toDados());
        if (repository.existsBySku(peca.sku(), null)) {
            throw new ConflictException("SKU_JA_CADASTRADO", "Já existe peça/insumo com este SKU");
        }
        return PecaInsumoOutput.from(repository.save(peca));
    }
}
