package br.com.oficina.entrypoints.controllers.cadastro;

import br.com.oficina.application.dto.cadastro.VeiculoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.usecase.cadastro.AtualizarVeiculoUseCase;
import br.com.oficina.application.usecase.cadastro.CadastrarVeiculoUseCase;
import br.com.oficina.application.usecase.cadastro.ListarVeiculosUseCase;
import br.com.oficina.application.usecase.cadastro.ObterVeiculoUseCase;
import br.com.oficina.application.usecase.cadastro.RemoverVeiculoUseCase;
import br.com.oficina.contract.api.VeiculosApi;
import br.com.oficina.contract.model.PaginaVeiculosDto;
import br.com.oficina.contract.model.VeiculoRequestDto;
import br.com.oficina.contract.model.VeiculoResponseDto;
import br.com.oficina.entrypoints.controllers.presenter.cadastro.VeiculoPresenter;
import br.com.oficina.infrastructure.dataproviders.config.security.SolicitanteAtual;
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
        VeiculoOutput veiculo = cadastrarVeiculo.execute(VeiculoPresenter.toInput(request), SolicitanteAtual.get());
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(veiculo.id())
                .toUri();
        return ResponseEntity.created(location).body(VeiculoPresenter.toResponse(veiculo));
    }

    @Override
    public ResponseEntity<VeiculoResponseDto> obterVeiculo(Integer veiculoId) {
        return ResponseEntity.ok(VeiculoPresenter.toResponse(obterVeiculo.execute(veiculoId, SolicitanteAtual.get())));
    }

    @Override
    public ResponseEntity<PaginaVeiculosDto> listarVeiculos(Integer page, Integer size, String placa,
                                                            Integer clienteId) {
        PageResult<VeiculoOutput> pagina = listarVeiculos.execute(placa, clienteId, PageQuery.of(page, size),
                SolicitanteAtual.get());
        return ResponseEntity.ok(VeiculoPresenter.toPagina(pagina));
    }

    @Override
    public ResponseEntity<VeiculoResponseDto> atualizarVeiculo(Integer veiculoId, VeiculoRequestDto request) {
        return ResponseEntity.ok(VeiculoPresenter.toResponse(atualizarVeiculo.execute(veiculoId, VeiculoPresenter.toInput(request), SolicitanteAtual.get())));
    }

    @Override
    public ResponseEntity<Void> removerVeiculo(Integer veiculoId) {
        removerVeiculo.execute(veiculoId);
        return ResponseEntity.noContent().build();
    }
}
