package br.com.oficina.shared.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Value Object: valor em reais, sempre com 2 casas decimais e nunca negativo.
 *
 * <p>Como a validação está no construtor, é impossível existir um {@code Dinheiro} inválido no sistema —
 * quem recebe um não precisa validar de novo. Por ser um {@code record}, é imutável e dois objetos com o
 * mesmo valor são iguais.
 */
public record Dinheiro(BigDecimal valor) {

    public Dinheiro {
        if (valor == null || valor.signum() < 0) {
            throw new IllegalArgumentException("Valor monetário deve ser zero ou positivo");
        }
        valor = valor.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Dinheiro de(String valor) {
        return new Dinheiro(new BigDecimal(valor));
    }

    /** Formato do contrato: string decimal, ex. {@code "149.90"}. */
    @Override
    public String toString() {
        return valor.toPlainString();
    }
}
