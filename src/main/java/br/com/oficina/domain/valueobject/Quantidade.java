package br.com.oficina.domain.valueobject;

import br.com.oficina.domain.exceptions.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Quantidade(BigDecimal valor) {

    public Quantidade {
        if (valor == null || valor.signum() <= 0) {
            throw new ValidationException("Quantidade deve ser maior que zero");
        }
        valor = valor.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Quantidade de(BigDecimal valor) {
        return new Quantidade(valor);
    }

    @Override
    public String toString() {
        return valor.toPlainString();
    }
}
