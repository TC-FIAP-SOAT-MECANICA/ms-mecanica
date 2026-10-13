package br.com.oficina.cadastro.application.dto;

/** Dados de entrada do caso de uso. Tipos simples: não depende de HTTP nem de JSON. */
public record CadastrarClienteInput(String documento, String nome, String email, String telefone,
                                    EnderecoData endereco) {
}
