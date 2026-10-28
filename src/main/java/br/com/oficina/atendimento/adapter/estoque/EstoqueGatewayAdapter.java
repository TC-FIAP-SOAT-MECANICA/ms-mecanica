package br.com.oficina.atendimento.adapter.estoque;

import br.com.oficina.atendimento.application.port.EstoqueGateway;
import br.com.oficina.estoque.application.usecase.MovimentarEstoqueUseCase;
import br.com.oficina.estoque.domain.TipoMovimentacao;
import br.com.oficina.shared.domain.valueobject.Dinheiro;
import br.com.oficina.shared.domain.valueobject.Quantidade;
import org.springframework.stereotype.Component;

@Component
class EstoqueGatewayAdapter implements EstoqueGateway {

    private final MovimentarEstoqueUseCase movimentarEstoque;

    EstoqueGatewayAdapter(MovimentarEstoqueUseCase movimentarEstoque) {
        this.movimentarEstoque = movimentarEstoque;
    }

    @Override
    public Dinheiro baixar(Integer pecaInsumoId, Quantidade quantidade) {
        var peca = movimentarEstoque.execute(pecaInsumoId, TipoMovimentacao.SAIDA, quantidade.valor());
        return Dinheiro.de(peca.precoVenda());
    }

    @Override
    public void devolver(Integer pecaInsumoId, Quantidade quantidade) {
        movimentarEstoque.execute(pecaInsumoId, TipoMovimentacao.ENTRADA, quantidade.valor());
    }
}
