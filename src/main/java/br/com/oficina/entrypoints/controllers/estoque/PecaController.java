package br.com.oficina.entrypoints.controllers.estoque;

import br.com.oficina.application.dto.estoque.PecaInsumoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.usecase.estoque.AtualizarPecaInsumoUseCase;
import br.com.oficina.application.usecase.estoque.CadastrarPecaInsumoUseCase;
import br.com.oficina.application.usecase.estoque.ListarPecasInsumosUseCase;
import br.com.oficina.application.usecase.estoque.MovimentarEstoqueUseCase;
import br.com.oficina.application.usecase.estoque.ObterPecaInsumoUseCase;
import br.com.oficina.application.usecase.estoque.RemoverPecaInsumoUseCase;
import br.com.oficina.contract.api.PecasApi;
import br.com.oficina.contract.model.MovimentacaoEstoqueRequestDto;
import br.com.oficina.contract.model.PaginaPecasDto;
import br.com.oficina.contract.model.PecaRequestDto;
import br.com.oficina.contract.model.PecaResponseDto;
import br.com.oficina.entrypoints.controllers.presenter.estoque.PecaPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
public class PecaController implements PecasApi {

    private final CadastrarPecaInsumoUseCase cadastrarPeca;
    private final ObterPecaInsumoUseCase obterPeca;
    private final ListarPecasInsumosUseCase listarPecas;
    private final AtualizarPecaInsumoUseCase atualizarPeca;
    private final RemoverPecaInsumoUseCase removerPeca;
    private final MovimentarEstoqueUseCase movimentarEstoque;

    public PecaController(CadastrarPecaInsumoUseCase cadastrarPeca, ObterPecaInsumoUseCase obterPeca,
                          ListarPecasInsumosUseCase listarPecas, AtualizarPecaInsumoUseCase atualizarPeca,
                          RemoverPecaInsumoUseCase removerPeca, MovimentarEstoqueUseCase movimentarEstoque) {
        this.cadastrarPeca = cadastrarPeca;
        this.obterPeca = obterPeca;
        this.listarPecas = listarPecas;
        this.atualizarPeca = atualizarPeca;
        this.removerPeca = removerPeca;
        this.movimentarEstoque = movimentarEstoque;
    }

    @Override
    public ResponseEntity<PecaResponseDto> cadastrarPeca(UUID idempotencyKey, PecaRequestDto request) {
        PecaInsumoOutput peca = cadastrarPeca.execute(PecaPresenter.toInput(request));
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(peca.id())
                .toUri();
        return ResponseEntity.created(location).body(PecaPresenter.toResponse(peca));
    }

    @Override
    public ResponseEntity<PecaResponseDto> obterPeca(Integer pecaId) {
        return ResponseEntity.ok(PecaPresenter.toResponse(obterPeca.execute(pecaId)));
    }

    @Override
    public ResponseEntity<PaginaPecasDto> listarPecas(Integer page, Integer size, String nome,
                                                      Boolean abaixoDoMinimo) {
        PageResult<PecaInsumoOutput> pagina = listarPecas.execute(nome, abaixoDoMinimo, PageQuery.of(page, size));
        return ResponseEntity.ok(PecaPresenter.toPagina(pagina));
    }

    @Override
    public ResponseEntity<PecaResponseDto> atualizarPeca(Integer pecaId, PecaRequestDto request) {
        return ResponseEntity.ok(PecaPresenter.toResponse(atualizarPeca.execute(pecaId, PecaPresenter.toInput(request))));
    }

    @Override
    public ResponseEntity<Void> removerPeca(Integer pecaId) {
        removerPeca.execute(pecaId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PecaResponseDto> registrarMovimentacaoEstoque(Integer pecaId, UUID idempotencyKey,
                                                                        MovimentacaoEstoqueRequestDto request) {
        PecaInsumoOutput peca = movimentarEstoque.execute(pecaId,
                request.getTipo().getValue(), request.getQuantidade());
        return ResponseEntity.ok(PecaPresenter.toResponse(peca));
    }
}
