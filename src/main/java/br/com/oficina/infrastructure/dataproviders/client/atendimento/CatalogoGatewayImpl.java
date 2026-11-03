package br.com.oficina.infrastructure.dataproviders.client.atendimento;

import br.com.oficina.application.gateways.atendimento.CatalogoGateway;
import br.com.oficina.application.usecase.catalogo.ObterServicoUseCase;
import br.com.oficina.domain.valueobject.Dinheiro;
import org.springframework.stereotype.Component;

@Component
class CatalogoGatewayImpl implements CatalogoGateway {

    private final ObterServicoUseCase obterServico;

    CatalogoGatewayImpl(ObterServicoUseCase obterServico) {
        this.obterServico = obterServico;
    }

    @Override
    public Dinheiro precoBaseDoServico(Integer servicoId) {
        return Dinheiro.de(obterServico.execute(servicoId).precoBase());
    }
}
