package br.com.oficina.entrypoints.controllers.presenter.atendimento;

import br.com.oficina.application.dto.atendimento.AbrirOrdemServicoInput;
import br.com.oficina.application.dto.atendimento.ItemPecaInput;
import br.com.oficina.application.dto.atendimento.ItemServicoInput;
import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.contract.model.AbrirOrdemServicoRequestDto;
import br.com.oficina.contract.model.ItemPecaRequestDto;
import br.com.oficina.contract.model.ItemPecaResponseDto;
import br.com.oficina.contract.model.ItemServicoRequestDto;
import br.com.oficina.contract.model.ItemServicoResponseDto;
import br.com.oficina.contract.model.OrdemServicoResponseDto;
import br.com.oficina.contract.model.PaginaOrdensServicoDto;
import br.com.oficina.contract.model.StatusOSDto;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

public final class OrdemServicoPresenter {

    private OrdemServicoPresenter() {
    }

    public static AbrirOrdemServicoInput toInput(AbrirOrdemServicoRequestDto request) {
        return new AbrirOrdemServicoInput(request.getClienteId(), request.getVeiculoId(),
                request.getDescricaoProblema(),
                request.getServicos().stream().map(OrdemServicoPresenter::toInput).toList(),
                request.getPecas().stream().map(OrdemServicoPresenter::toInput).toList());
    }

    public static ItemServicoInput toInput(ItemServicoRequestDto request) {
        return new ItemServicoInput(request.getServicoId(), request.getPrecoFinal());
    }

    public static ItemPecaInput toInput(ItemPecaRequestDto request) {
        return new ItemPecaInput(request.getPecaInsumoId(), request.getQuantidade());
    }

    public static List<String> toFiltroStatus(Set<StatusOSDto> status) {
        return status == null ? List.of() : status.stream().map(StatusOSDto::getValue).toList();
    }

    public static OrdemServicoResponseDto toResponse(OrdemServicoOutput os) {
        return new OrdemServicoResponseDto()
                .id(os.id())
                .clienteId(os.clienteId())
                .veiculoId(os.veiculoId())
                .status(StatusOSDto.fromValue(os.status()))
                .descricaoProblema(os.descricaoProblema())
                .diagnosticoTecnico(os.diagnosticoTecnico())
                .valorTotal(os.valorTotal())
                .dataAbertura(toOffsetDateTime(os.dataAbertura()))
                .dataConclusao(toOffsetDateTime(os.dataConclusao()))
                .servicos(os.servicos().stream().map(OrdemServicoPresenter::toResponse).toList())
                .pecas(os.pecas().stream().map(OrdemServicoPresenter::toResponse).toList());
    }

    public static PaginaOrdensServicoDto toPagina(PageResult<OrdemServicoOutput> pagina) {
        return new PaginaOrdensServicoDto()
                .page(pagina.page())
                .size(pagina.size())
                .totalElements(pagina.totalElements())
                .totalPages(pagina.totalPages())
                .content(pagina.content().stream().map(OrdemServicoPresenter::toResponse).toList());
    }

    private static ItemServicoResponseDto toResponse(OrdemServicoOutput.Servico item) {
        return new ItemServicoResponseDto()
                .id(item.id())
                .servicoId(item.servicoId())
                .precoFinal(item.precoFinal());
    }

    private static ItemPecaResponseDto toResponse(OrdemServicoOutput.Peca item) {
        return new ItemPecaResponseDto()
                .id(item.id())
                .pecaInsumoId(item.pecaInsumoId())
                .quantidade(item.quantidade())
                .precoUnitario(item.precoUnitario())
                .subtotal(item.subtotal());
    }

    private static OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant == null ? null : instant.atOffset(ZoneOffset.UTC);
    }
}
