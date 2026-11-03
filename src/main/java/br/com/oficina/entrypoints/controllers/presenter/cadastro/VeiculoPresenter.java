package br.com.oficina.entrypoints.controllers.presenter.cadastro;

import br.com.oficina.application.dto.cadastro.VeiculoInput;
import br.com.oficina.application.dto.cadastro.VeiculoOutput;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.contract.model.PaginaVeiculosDto;
import br.com.oficina.contract.model.VeiculoRequestDto;
import br.com.oficina.contract.model.VeiculoResponseDto;

public final class VeiculoPresenter {

    private VeiculoPresenter() {
    }

    public static VeiculoInput toInput(VeiculoRequestDto request) {
        return new VeiculoInput(request.getClienteId(), request.getPlaca(), request.getMarca(), request.getModelo(),
                request.getAno(), request.getCor(), request.getChassi(), request.getQuilometragem());
    }

    public static VeiculoResponseDto toResponse(VeiculoOutput veiculo) {
        return new VeiculoResponseDto()
                .id(veiculo.id())
                .clienteId(veiculo.clienteId())
                .placa(veiculo.placa())
                .marca(veiculo.marca())
                .modelo(veiculo.modelo())
                .ano(veiculo.ano())
                .cor(veiculo.cor())
                .chassi(veiculo.chassi())
                .quilometragem(veiculo.quilometragem());
    }

    public static PaginaVeiculosDto toPagina(PageResult<VeiculoOutput> pagina) {
        return new PaginaVeiculosDto()
                .page(pagina.page())
                .size(pagina.size())
                .totalElements(pagina.totalElements())
                .totalPages(pagina.totalPages())
                .content(pagina.content().stream().map(VeiculoPresenter::toResponse).toList());
    }
}
