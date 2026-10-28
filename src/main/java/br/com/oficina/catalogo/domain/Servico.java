package br.com.oficina.catalogo.domain;

import br.com.oficina.shared.domain.valueobject.Dinheiro;

import java.util.Objects;

public class Servico {

    private final Integer id;
    private String nome;
    private String descricao;
    private Dinheiro precoBase;

    private Servico(Integer id, String nome, String descricao, Dinheiro precoBase) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.precoBase = precoBase;
    }

    public static Servico criar(String nome, String descricao, Dinheiro precoBase) {
        var servico = new Servico(null, null, null, null);
        servico.atualizar(nome, descricao, precoBase);
        return servico;
    }

    public static Servico restaurar(Integer id, String nome, String descricao, Dinheiro precoBase) {
        return new Servico(id, nome, descricao, precoBase);
    }

    public void atualizar(String nome, String descricao, Dinheiro precoBase) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do serviço é obrigatório");
        }
        this.nome = nome.trim();
        this.descricao = descricao == null || descricao.isBlank() ? null : descricao.trim();
        this.precoBase = Objects.requireNonNull(precoBase, "precoBase");
    }

    public Integer id() {
        return id;
    }

    public String nome() {
        return nome;
    }

    public String descricao() {
        return descricao;
    }

    public Dinheiro precoBase() {
        return precoBase;
    }
}
