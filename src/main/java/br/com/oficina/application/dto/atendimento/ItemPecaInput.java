package br.com.oficina.application.dto.atendimento;

import java.math.BigDecimal;

public record ItemPecaInput(Integer pecaInsumoId, BigDecimal quantidade) {
}
