package br.com.oficina.shared.domain.exception;

public class ForbiddenException extends DomainException {

    public ForbiddenException(String message) {
        super("ACESSO_NEGADO", message);
    }
}
