package br.com.oficina.application.gateways.atendimento;

import br.com.oficina.domain.valueobject.Dinheiro;
import br.com.oficina.domain.valueobject.Quantidade;

public interface EstoqueGateway {

    Dinheiro baixar(Integer pecaInsumoId, Quantidade quantidade);

    void devolver(Integer pecaInsumoId, Quantidade quantidade);
}
