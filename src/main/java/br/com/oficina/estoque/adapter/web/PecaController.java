package br.com.oficina.estoque.adapter.web;

import br.com.oficina.contract.api.PecasApi;
import br.com.oficina.contract.model.MovimentacaoEstoqueRequestDto;
import br.com.oficina.contract.model.PaginaPecasDto;
import br.com.oficina.contract.model.PecaRequestDto;
import br.com.oficina.contract.model.PecaResponseDto;
import br.com.oficina.estoque.application.dto.PecaInsumoInput;
import br.com.oficina.estoque.application.dto.PecaInsumoOutput;
import br.com.oficina.estoque.application.usecase.AtualizarPecaInsumoUseCase;
import br.com.oficina.estoque.application.usecase.CadastrarPecaInsumoUseCase;
import br.com.oficina.estoque.application.usecase.ListarPecasInsumosUseCase;
import br.com.oficina.estoque.application.usecase.MovimentarEstoqueUseCase;
import br.com.oficina.estoque.application.usecase.ObterPecaInsumoUseCase;
import br.com.oficina.estoque.application.usecase.RemoverPecaInsumoUseCase;
import br.com.oficina.estoque.domain.TipoMovimentacao;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
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
        PecaInsumoOutput peca = cadastrarPeca.execute(toInput(request));
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(peca.id())
                .toUri();
        return ResponseEntity.created(location).body(toResponse(peca));
    }

    @Override
    public ResponseEntity<PecaResponseDto> obterPeca(Integer pecaId) {
        return ResponseEntity.ok(toResponse(obterPeca.execute(pecaId)));
    }

    @Override
    public ResponseEntity<PaginaPecasDto> listarPecas(Integer page, Integer size, String nome,
                                                      Boolean abaixoDoMinimo) {
        PageResult<PecaInsumoOutput> pagina = listarPecas.execute(nome, abaixoDoMinimo, PageQuery.of(page, size));
        return ResponseEntity.ok(new PaginaPecasDto()
                .page(pagina.page())
                .size(pagina.size())
                .totalElements(pagina.totalElements())
                .totalPages(pagina.totalPages())
                .content(pagina.content().stream().map(PecaController::toResponse).toList()));
    }

    @Override
    public ResponseEntity<PecaResponseDto> atualizarPeca(Integer pecaId, PecaRequestDto request) {
        return ResponseEntity.ok(toResponse(atualizarPeca.execute(pecaId, toInput(request))));
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
                TipoMovimentacao.valueOf(request.getTipo().getValue()), request.getQuantidade());
        return ResponseEntity.ok(toResponse(peca));
    }

    private static PecaInsumoInput toInput(PecaRequestDto request) {
        return new PecaInsumoInput(request.getSku(), request.getNome(), request.getUnidadeMedida(),
                request.getPrecoCusto(), request.getPrecoVenda(), request.getEstoqueMinimo());
    }

    private static PecaResponseDto toResponse(PecaInsumoOutput peca) {
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
}
