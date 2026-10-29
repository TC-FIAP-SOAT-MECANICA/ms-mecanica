package br.com.oficina.application.exceptions;

public class ForbiddenException extends ApplicationException {

    public ForbiddenException(String message) {
        super("ACESSO_NEGADO", message);
    }
}
