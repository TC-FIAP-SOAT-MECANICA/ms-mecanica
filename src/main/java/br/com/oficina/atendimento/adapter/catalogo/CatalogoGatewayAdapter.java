package br.com.oficina.atendimento.adapter.catalogo;

import br.com.oficina.atendimento.application.port.CatalogoGateway;
import br.com.oficina.catalogo.application.usecase.ObterServicoUseCase;
import br.com.oficina.shared.domain.valueobject.Dinheiro;
import org.springframework.stereotype.Component;

@Component
class CatalogoGatewayAdapter implements CatalogoGateway {

    private final ObterServicoUseCase obterServico;

    CatalogoGatewayAdapter(ObterServicoUseCase obterServico) {
        this.obterServico = obterServico;
    }

    @Override
    public Dinheiro precoBaseDoServico(Integer servicoId) {
        return Dinheiro.de(obterServico.execute(servicoId).precoBase());
    }
}
