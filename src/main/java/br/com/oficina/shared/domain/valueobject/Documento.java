package br.com.oficina.shared.domain.valueobject;

/**
 * Value Object: CPF (11 dígitos) ou CNPJ (14 dígitos), validado pelos dígitos verificadores.
 *
 * <p>O contrato só confere o formato (quantidade de dígitos); a regra dos dígitos verificadores é de negócio
 * e fica aqui. Se o objeto existe, o documento é válido.
 */
public record Documento(String valor) {

    public enum Tipo { CPF, CNPJ }

    public Documento {
        if (valor == null) {
            throw new IllegalArgumentException("CPF/CNPJ é obrigatório");
        }
        valor = valor.replaceAll("\\D", "");
        boolean valido = switch (valor.length()) {
            case 11 -> cpfValido(valor);
            case 14 -> cnpjValido(valor);
            default -> false;
        };
        if (!valido) {
            throw new IllegalArgumentException("CPF/CNPJ inválido");
        }
    }

    public Tipo tipo() {
        return valor.length() == 11 ? Tipo.CPF : Tipo.CNPJ;
    }

    /** Nunca imprime o documento completo em logs (LGPD). */
    @Override
    public String toString() {
        return "Documento[" + tipo() + " ***" + valor.substring(valor.length() - 2) + "]";
    }

    private static boolean cpfValido(String cpf) {
        if (todosIguais(cpf)) {
            return false;
        }
        return digitoCpf(cpf, 9) == cpf.charAt(9) - '0' && digitoCpf(cpf, 10) == cpf.charAt(10) - '0';
    }

    private static int digitoCpf(String cpf, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (cpf.charAt(i) - '0') * (tamanho + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }

    private static boolean cnpjValido(String cnpj) {
        if (todosIguais(cnpj)) {
            return false;
        }
        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        return digitoCnpj(cnpj, pesos1) == cnpj.charAt(12) - '0' && digitoCnpj(cnpj, pesos2) == cnpj.charAt(13) - '0';
    }

    private static int digitoCnpj(String cnpj, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += (cnpj.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    private static boolean todosIguais(String s) {
        return s.chars().allMatch(c -> c == s.charAt(0));
    }
}
