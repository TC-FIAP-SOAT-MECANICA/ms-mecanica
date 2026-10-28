package br.com.oficina.atendimento.application.dto;

import java.math.BigDecimal;

public record ItemPecaInput(Integer pecaInsumoId, BigDecimal quantidade) {
}
