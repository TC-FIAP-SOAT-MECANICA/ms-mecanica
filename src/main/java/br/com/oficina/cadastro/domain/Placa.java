package br.com.oficina.cadastro.domain;

import java.util.regex.Pattern;

public record Placa(String valor) {

    private static final Pattern FORMATO = Pattern.compile("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$");

    public Placa {
        if (valor == null) {
            throw new IllegalArgumentException("Placa é obrigatória");
        }
        valor = valor.replace("-", "").replace(" ", "").toUpperCase();
        if (!FORMATO.matcher(valor).matches()) {
            throw new IllegalArgumentException("Placa deve seguir o padrão ABC1234 ou ABC1D23");
        }
    }

    @Override
    public String toString() {
        return valor;
    }
}
