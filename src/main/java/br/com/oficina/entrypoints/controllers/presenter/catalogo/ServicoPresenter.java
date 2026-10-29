package br.com.oficina.entrypoints.controllers.presenter.catalogo;

import br.com.oficina.application.dto.catalogo.ServicoInput;
import br.com.oficina.application.dto.catalogo.ServicoOutput;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.contract.model.PaginaServicosDto;
import br.com.oficina.contract.model.ServicoRequestDto;
import br.com.oficina.contract.model.ServicoResponseDto;

public final class ServicoPresenter {

    private ServicoPresenter() {
    }

    public static ServicoInput toInput(ServicoRequestDto request) {
        return new ServicoInput(request.getNome(), request.getDescricao(), request.getPrecoBase());
    }

    public static ServicoResponseDto toResponse(ServicoOutput servico) {
        return new ServicoResponseDto()
                .id(servico.id())
                .nome(servico.nome())
                .descricao(servico.descricao())
                .precoBase(servico.precoBase());
    }

    public static PaginaServicosDto toPagina(PageResult<ServicoOutput> pagina) {
        return new PaginaServicosDto()
                .page(pagina.page())
                .size(pagina.size())
                .totalElements(pagina.totalElements())
                .totalPages(pagina.totalPages())
                .content(pagina.content().stream().map(ServicoPresenter::toResponse).toList());
    }
}
