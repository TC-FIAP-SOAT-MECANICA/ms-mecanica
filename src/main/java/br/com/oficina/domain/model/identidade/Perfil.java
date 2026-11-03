package br.com.oficina.domain.model.identidade;

public enum Perfil {
    ADMIN,
    MECANICO,
    CLIENTE;

    public boolean funcionario() {
        return this != CLIENTE;
    }
}
