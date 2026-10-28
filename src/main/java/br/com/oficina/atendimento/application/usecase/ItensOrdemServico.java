package br.com.oficina.atendimento.application.usecase;

import br.com.oficina.atendimento.application.dto.ItemPecaInput;
import br.com.oficina.atendimento.application.dto.ItemServicoInput;
import br.com.oficina.atendimento.application.port.CatalogoGateway;
import br.com.oficina.atendimento.application.port.EstoqueGateway;
import br.com.oficina.atendimento.domain.OrdemServico;
import br.com.oficina.shared.domain.valueobject.Dinheiro;
import br.com.oficina.shared.domain.valueobject.Quantidade;

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
