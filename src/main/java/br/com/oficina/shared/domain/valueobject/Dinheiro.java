package br.com.oficina.shared.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Dinheiro(BigDecimal valor) {

    public static final Dinheiro ZERO = new Dinheiro(BigDecimal.ZERO);

    public Dinheiro {
        if (valor == null || valor.signum() < 0) {
            throw new IllegalArgumentException("Valor monetário deve ser zero ou positivo");
        }
        valor = valor.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Dinheiro de(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor monetário é obrigatório");
        }
        return new Dinheiro(new BigDecimal(valor));
    }

    public Dinheiro somar(Dinheiro outro) {
        return new Dinheiro(valor.add(outro.valor));
    }

    public Dinheiro multiplicar(Quantidade quantidade) {
        return new Dinheiro(valor.multiply(quantidade.valor()));
    }

    @Override
    public String toString() {
        return valor.toPlainString();
    }
}
