package br.com.oficina.catalogo.domain;

import br.com.oficina.shared.domain.valueobject.Dinheiro;

import java.util.Objects;
import java.util.UUID;

/**
 * Entidade de domínio: um serviço oferecido pela oficina.
 *
 * <p>Repare que esta classe é Java puro: não tem {@code @Entity}, {@code @Table} nem nada do Spring.
 * As regras de negócio ficam aqui e continuam valendo mesmo se o banco ou o framework forem trocados.
 */
public class Servico {

    private static final int TEMPO_MAXIMO_MINUTOS = 10_080; // 7 dias

    private final UUID id;
    private final String nome;
    private final String descricao;
    private final Dinheiro preco;
    private final int tempoEstimadoMinutos;
    private final boolean ativo;

    private Servico(UUID id, String nome, String descricao, Dinheiro preco, int tempoEstimadoMinutos,
                    boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.tempoEstimadoMinutos = tempoEstimadoMinutos;
        this.ativo = ativo;
    }

    /** Cria um serviço novo aplicando as regras de negócio: todo serviço novo nasce ativo e com id próprio. */
    public static Servico criar(String nome, String descricao, Dinheiro preco, int tempoEstimadoMinutos) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do serviço é obrigatório");
        }
        if (tempoEstimadoMinutos < 1 || tempoEstimadoMinutos > TEMPO_MAXIMO_MINUTOS) {
            throw new IllegalArgumentException("Tempo estimado deve estar entre 1 minuto e 7 dias");
        }
        return new Servico(UUID.randomUUID(), nome.trim(), descricao, Objects.requireNonNull(preco, "preco"),
                tempoEstimadoMinutos, true);
    }

    public UUID id() {
        return id;
    }

    public String nome() {
        return nome;
    }

    public String descricao() {
        return descricao;
    }

    public Dinheiro preco() {
        return preco;
    }

    public int tempoEstimadoMinutos() {
        return tempoEstimadoMinutos;
    }

    public boolean ativo() {
        return ativo;
    }
}
