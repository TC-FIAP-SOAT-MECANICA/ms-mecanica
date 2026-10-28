package br.com.oficina.cadastro.adapter.web;

import br.com.oficina.cadastro.application.dto.VeiculoInput;
import br.com.oficina.cadastro.application.dto.VeiculoOutput;
import br.com.oficina.cadastro.application.usecase.AtualizarVeiculoUseCase;
import br.com.oficina.cadastro.application.usecase.CadastrarVeiculoUseCase;
import br.com.oficina.cadastro.application.usecase.ListarVeiculosUseCase;
import br.com.oficina.cadastro.application.usecase.ObterVeiculoUseCase;
import br.com.oficina.cadastro.application.usecase.RemoverVeiculoUseCase;
import br.com.oficina.contract.api.VeiculosApi;
import br.com.oficina.contract.model.PaginaVeiculosDto;
import br.com.oficina.contract.model.VeiculoRequestDto;
import br.com.oficina.contract.model.VeiculoResponseDto;
import br.com.oficina.shared.adapter.web.SolicitanteAtual;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
public class VeiculoController implements VeiculosApi {

    private final CadastrarVeiculoUseCase cadastrarVeiculo;
    private final ObterVeiculoUseCase obterVeiculo;
    private final ListarVeiculosUseCase listarVeiculos;
    private final AtualizarVeiculoUseCase atualizarVeiculo;
    private final RemoverVeiculoUseCase removerVeiculo;

    public VeiculoController(CadastrarVeiculoUseCase cadastrarVeiculo, ObterVeiculoUseCase obterVeiculo,
                             ListarVeiculosUseCase listarVeiculos, AtualizarVeiculoUseCase atualizarVeiculo,
                             RemoverVeiculoUseCase removerVeiculo) {
        this.cadastrarVeiculo = cadastrarVeiculo;
        this.obterVeiculo = obterVeiculo;
        this.listarVeiculos = listarVeiculos;
        this.atualizarVeiculo = atualizarVeiculo;
        this.removerVeiculo = removerVeiculo;
    }

    @Override
    public ResponseEntity<VeiculoResponseDto> cadastrarVeiculo(UUID idempotencyKey, VeiculoRequestDto request) {
        VeiculoOutput veiculo = cadastrarVeiculo.execute(toInput(request), SolicitanteAtual.get());
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(veiculo.id())
                .toUri();
        return ResponseEntity.created(location).body(toResponse(veiculo));
    }

    @Override
    public ResponseEntity<VeiculoResponseDto> obterVeiculo(Integer veiculoId) {
        return ResponseEntity.ok(toResponse(obterVeiculo.execute(veiculoId, SolicitanteAtual.get())));
    }

    @Override
    public ResponseEntity<PaginaVeiculosDto> listarVeiculos(Integer page, Integer size, String placa,
                                                            Integer clienteId) {
        PageResult<VeiculoOutput> pagina = listarVeiculos.execute(placa, clienteId, PageQuery.of(page, size),
                SolicitanteAtual.get());
        return ResponseEntity.ok(new PaginaVeiculosDto()
                .page(pagina.page())
                .size(pagina.size())
                .totalElements(pagina.totalElements())
                .totalPages(pagina.totalPages())
                .content(pagina.content().stream().map(VeiculoController::toResponse).toList()));
    }

    @Override
    public ResponseEntity<VeiculoResponseDto> atualizarVeiculo(Integer veiculoId, VeiculoRequestDto request) {
        return ResponseEntity.ok(toResponse(atualizarVeiculo.execute(veiculoId, toInput(request), SolicitanteAtual.get())));
    }

    @Override
    public ResponseEntity<Void> removerVeiculo(Integer veiculoId) {
        removerVeiculo.execute(veiculoId);
        return ResponseEntity.noContent().build();
    }

    private static VeiculoInput toInput(VeiculoRequestDto request) {
        return new VeiculoInput(request.getClienteId(), request.getPlaca(), request.getMarca(), request.getModelo(),
                request.getAno(), request.getCor(), request.getChassi(), request.getQuilometragem());
    }

    private static VeiculoResponseDto toResponse(VeiculoOutput veiculo) {
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
}
