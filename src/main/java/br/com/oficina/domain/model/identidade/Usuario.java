package br.com.oficina.domain.model.identidade;

import br.com.oficina.domain.exceptions.ValidationException;

import java.time.Instant;
import java.util.Objects;

public class Usuario {

    private final Integer id;
    private String nome;
    private String email;
    private String telefone;
    private final String senhaHash;
    private final Perfil perfil;
    private final Instant criadoEm;

    private Usuario(Integer id, String nome, String email, String telefone, String senhaHash, Perfil perfil,
                    Instant criadoEm) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
        this.criadoEm = criadoEm;
    }

    public static Usuario cadastrar(String nome, String email, String telefone, String senhaHash, Perfil perfil,
                                    Instant agora) {
        var usuario = new Usuario(null, null, null, null, obrigatorio(senhaHash, "Senha"),
                Objects.requireNonNull(perfil, "perfil"), agora);
        usuario.atualizarContato(nome, email, telefone);
        return usuario;
    }

    public static Usuario restaurar(Integer id, String nome, String email, String telefone, String senhaHash,
                                    Perfil perfil, Instant criadoEm) {
        return new Usuario(id, nome, email, telefone, senhaHash, perfil, criadoEm);
    }

    public void atualizarContato(String nome, String email, String telefone) {
        this.nome = obrigatorio(nome, "Nome").trim();
        this.email = normalizarEmail(email);
        this.telefone = obrigatorio(telefone, "Telefone").trim();
    }

    public static String normalizarEmail(String email) {
        return obrigatorio(email, "E-mail").trim().toLowerCase();
    }

    private static String obrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ValidationException(campo + " é obrigatório");
        }
        return valor;
    }

    public Integer id() {
        return id;
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

    public String senhaHash() {
        return senhaHash;
    }

    public Perfil perfil() {
        return perfil;
    }

    public Instant criadoEm() {
        return criadoEm;
    }
}
