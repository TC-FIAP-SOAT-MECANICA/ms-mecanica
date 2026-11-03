package br.com.oficina.domain.model.estoque;

import br.com.oficina.domain.exceptions.BusinessRuleException;
import br.com.oficina.domain.exceptions.ValidationException;

import java.math.BigDecimal;
import java.util.Set;
import java.util.regex.Pattern;

public record UnidadeMedida(String codigo) {

    private static final Pattern FORMATO = Pattern.compile("^[A-Z]{1,10}$");
    private static final Set<String> CONTAVEIS = Set.of("UN", "PC", "PCT", "CX", "KIT", "PAR", "JG");

    public UnidadeMedida {
        codigo = codigo == null ? "" : codigo.trim().toUpperCase();
        if (!FORMATO.matcher(codigo).matches()) {
            throw new ValidationException("Unidade de medida inválida (ex.: UN, LT, KG)");
        }
    }

    public boolean aceitaFracao() {
        return !CONTAVEIS.contains(codigo);
    }

    public void validar(BigDecimal quantidade) {
        if (!aceitaFracao() && quantidade.stripTrailingZeros().scale() > 0) {
            throw new BusinessRuleException("QUANTIDADE_FRACIONADA",
                    "Unidade " + codigo + " só aceita quantidade inteira, recebido " + quantidade.toPlainString());
        }
    }
}
