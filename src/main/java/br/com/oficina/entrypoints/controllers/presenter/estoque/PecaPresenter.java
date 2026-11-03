package br.com.oficina.entrypoints.controllers.presenter.estoque;

import br.com.oficina.application.dto.estoque.PecaInsumoInput;
import br.com.oficina.application.dto.estoque.PecaInsumoOutput;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.contract.model.PaginaPecasDto;
import br.com.oficina.contract.model.PecaRequestDto;
import br.com.oficina.contract.model.PecaResponseDto;

public final class PecaPresenter {

    private PecaPresenter() {
    }

    public static PecaInsumoInput toInput(PecaRequestDto request) {
        return new PecaInsumoInput(request.getSku(), request.getNome(), request.getUnidadeMedida(),
                request.getPrecoCusto(), request.getPrecoVenda(), request.getEstoqueMinimo());
    }

    public static PecaResponseDto toResponse(PecaInsumoOutput peca) {
        return new PecaResponseDto()
                .id(peca.id())
                .sku(peca.sku())
                .nome(peca.nome())
                .unidadeMedida(peca.unidadeMedida())
                .precoCusto(peca.precoCusto())
                .precoVenda(peca.precoVenda())
                .quantidadeEstoque(peca.quantidadeEstoque())
                .estoqueMinimo(peca.estoqueMinimo())
                .abaixoDoMinimo(peca.abaixoDoMinimo());
    }

    public static PaginaPecasDto toPagina(PageResult<PecaInsumoOutput> pagina) {
        return new PaginaPecasDto()
                .page(pagina.page())
                .size(pagina.size())
                .totalElements(pagina.totalElements())
                .totalPages(pagina.totalPages())
                .content(pagina.content().stream().map(PecaPresenter::toResponse).toList());
    }
}
