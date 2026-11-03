package br.com.oficina.entrypoints.controllers.presenter.cadastro;

import br.com.oficina.application.dto.cadastro.AtualizarClienteInput;
import br.com.oficina.application.dto.cadastro.CadastrarClienteInput;
import br.com.oficina.application.dto.cadastro.ClienteOutput;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.contract.model.AtualizarClienteRequestDto;
import br.com.oficina.contract.model.ClienteRequestDto;
import br.com.oficina.contract.model.ClienteResponseDto;
import br.com.oficina.contract.model.PaginaClientesDto;
import br.com.oficina.contract.model.TipoDocumentoDto;

import java.time.ZoneOffset;

public final class ClientePresenter {

    private ClientePresenter() {
    }

    public static CadastrarClienteInput toInput(ClienteRequestDto request) {
        return new CadastrarClienteInput(request.getDocumento(), request.getNome(), request.getEmail(),
                request.getTelefone(), request.getSenha());
    }

    public static AtualizarClienteInput toInput(Integer clienteId, AtualizarClienteRequestDto request) {
        return new AtualizarClienteInput(clienteId, request.getNome(), request.getEmail(), request.getTelefone());
    }

    public static ClienteResponseDto toResponse(ClienteOutput cliente) {
        return new ClienteResponseDto()
                .id(cliente.id())
                .usuarioId(cliente.usuarioId())
                .tipoDocumento(TipoDocumentoDto.fromValue(cliente.tipoDocumento()))
                .documento(cliente.documento())
                .nome(cliente.nome())
                .email(cliente.email())
                .telefone(cliente.telefone())
                .criadoEm(cliente.criadoEm() == null ? null : cliente.criadoEm().atOffset(ZoneOffset.UTC));
    }

    public static PaginaClientesDto toPagina(PageResult<ClienteOutput> pagina) {
        return new PaginaClientesDto()
                .page(pagina.page())
                .size(pagina.size())
                .totalElements(pagina.totalElements())
                .totalPages(pagina.totalPages())
                .content(pagina.content().stream().map(ClientePresenter::toResponse).toList());
    }
}
