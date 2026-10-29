package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.ItemPecaInput;
import br.com.oficina.application.dto.atendimento.ItemServicoInput;
import br.com.oficina.application.gateways.atendimento.CatalogoGateway;
import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.domain.model.atendimento.OrdemServico;
import br.com.oficina.domain.valueobject.Dinheiro;
import br.com.oficina.domain.valueobject.Quantidade;

class ItensOrdemServico {

    private final CatalogoGateway catalogo;
    private final EstoqueGateway estoque;

    ItensOrdemServico(CatalogoGateway catalogo, EstoqueGateway estoque) {
        this.catalogo = catalogo;
        this.estoque = estoque;
    }

    void incluirServico(OrdemServico ordemServico, ItemServicoInput input) {
        ordemServico.garantirItensEditaveis();
        Dinheiro precoBase = catalogo.precoBaseDoServico(input.servicoId());
        Dinheiro precoFinal = input.precoFinal() == null ? precoBase : Dinheiro.de(input.precoFinal());
        ordemServico.incluirServico(input.servicoId(), precoFinal);
    }

    void incluirPeca(OrdemServico ordemServico, ItemPecaInput input) {
        ordemServico.garantirItensEditaveis();
        Quantidade quantidade = Quantidade.de(input.quantidade());
        Dinheiro precoVenda = estoque.baixar(input.pecaInsumoId(), quantidade);
        ordemServico.incluirPeca(input.pecaInsumoId(), quantidade, precoVenda);
    }
}
