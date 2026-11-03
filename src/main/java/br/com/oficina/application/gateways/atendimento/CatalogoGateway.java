package br.com.oficina.application.gateways.atendimento;

import br.com.oficina.domain.valueobject.Dinheiro;

public interface CatalogoGateway {

    Dinheiro precoBaseDoServico(Integer servicoId);
}
