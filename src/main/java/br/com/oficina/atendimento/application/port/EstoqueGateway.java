package br.com.oficina.atendimento.application.port;

import br.com.oficina.shared.domain.valueobject.Dinheiro;
import br.com.oficina.shared.domain.valueobject.Quantidade;

public interface EstoqueGateway {

    Dinheiro baixar(Integer pecaInsumoId, Quantidade quantidade);

    void devolver(Integer pecaInsumoId, Quantidade quantidade);
}
