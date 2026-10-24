package br.com.oficina.cadastro.domain;

/** Value Object: endereço do cliente. Todos os campos são opcionais no contrato. */
public record Endereco(String logradouro, String numero, String complemento, String bairro, String cidade,
                       String uf, String cep) {
}
