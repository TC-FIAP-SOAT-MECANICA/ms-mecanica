package br.com.oficina.atendimento.application.port;

import br.com.oficina.shared.domain.valueobject.Dinheiro;

public interface CatalogoGateway {

    Dinheiro precoBaseDoServico(Integer servicoId);
}
