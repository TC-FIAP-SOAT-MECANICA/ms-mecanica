package br.com.oficina.infrastructure.dataproviders.client.atendimento;

import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.application.usecase.estoque.CotarPecaInsumoUseCase;
import br.com.oficina.application.usecase.estoque.MovimentarEstoqueUseCase;
import br.com.oficina.domain.valueobject.Dinheiro;
import br.com.oficina.domain.valueobject.Quantidade;
import org.springframework.stereotype.Component;

@Component
class EstoqueGatewayImpl implements EstoqueGateway {

    private static final String SAIDA = "SAIDA";
    private static final String ENTRADA = "ENTRADA";

    private final CotarPecaInsumoUseCase cotarPeca;
    private final MovimentarEstoqueUseCase movimentarEstoque;

    EstoqueGatewayImpl(CotarPecaInsumoUseCase cotarPeca, MovimentarEstoqueUseCase movimentarEstoque) {
        this.cotarPeca = cotarPeca;
        this.movimentarEstoque = movimentarEstoque;
    }

    @Override
    public Dinheiro cotar(Integer pecaInsumoId, Quantidade quantidade) {
        return Dinheiro.de(cotarPeca.execute(pecaInsumoId, quantidade.valor()).precoVenda());
    }

    @Override
    public Dinheiro baixar(Integer pecaInsumoId, Quantidade quantidade) {
        var peca = movimentarEstoque.execute(pecaInsumoId, SAIDA, quantidade.valor());
        return Dinheiro.de(peca.precoVenda());
    }

    @Override
    public void devolver(Integer pecaInsumoId, Quantidade quantidade) {
        movimentarEstoque.execute(pecaInsumoId, ENTRADA, quantidade.valor());
    }
}
