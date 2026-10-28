package br.com.oficina.atendimento.adapter.web;

import br.com.oficina.atendimento.application.dto.OrdemServicoOutput;
import br.com.oficina.contract.model.ItemPecaResponseDto;
import br.com.oficina.contract.model.ItemServicoResponseDto;
import br.com.oficina.contract.model.OrdemServicoResponseDto;
import br.com.oficina.contract.model.StatusOSDto;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

final class OrdemServicoMapper {

    private OrdemServicoMapper() {
    }

    static OrdemServicoResponseDto toResponse(OrdemServicoOutput os) {
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
                .servicos(os.servicos().stream().map(OrdemServicoMapper::toResponse).toList())
                .pecas(os.pecas().stream().map(OrdemServicoMapper::toResponse).toList());
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
