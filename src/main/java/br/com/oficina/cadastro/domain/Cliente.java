package br.com.oficina.cadastro.domain;

import br.com.oficina.shared.domain.valueobject.Documento;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade de domínio: cliente da oficina (Java puro, sem JPA nem Spring).
 *
 * <p>Há duas formas de obter um {@code Cliente}:
 * <ul>
 *   <li>{@link #cadastrar} — cliente novo: aplica as regras e gera o id;</li>
 *   <li>{@link #restaurar} — cliente que já existe no banco: só remonta o objeto (usado pela persistência).</li>
 * </ul>
 */
public class Cliente {

    private final UUID id;
    private final Documento documento;
    private final String nome;
    private final String email;
    private final String telefone;
    private final Endereco endereco;
    private final Instant criadoEm;
    private final Instant atualizadoEm;

    private Cliente(UUID id, Documento documento, String nome, String email, String telefone, Endereco endereco,
                    Instant criadoEm, Instant atualizadoEm) {
        this.id = id;
        this.documento = documento;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Cliente cadastrar(Documento documento, String nome, String email, String telefone,
                                    Endereco endereco, Instant agora) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail do cliente é obrigatório");
        }
        return new Cliente(UUID.randomUUID(), Objects.requireNonNull(documento, "documento"), nome.trim(),
                email.trim().toLowerCase(), telefone, endereco, agora, agora);
    }

    public static Cliente restaurar(UUID id, Documento documento, String nome, String email, String telefone,
                                    Endereco endereco, Instant criadoEm, Instant atualizadoEm) {
        return new Cliente(id, documento, nome, email, telefone, endereco, criadoEm, atualizadoEm);
    }

    public UUID id() {
        return id;
    }

    public Documento documento() {
        return documento;
    }

    public String nome() {
        return nome;
    }

    public String email() {
        return email;
    }

    public String telefone() {
        return telefone;
    }

    public Endereco endereco() {
        return endereco;
    }

    public Instant criadoEm() {
        return criadoEm;
    }

    public Instant atualizadoEm() {
        return atualizadoEm;
    }
}
