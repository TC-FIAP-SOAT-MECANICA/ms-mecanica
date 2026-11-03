package br.com.oficina.application.exceptions;

public class UnauthorizedException extends ApplicationException {

    public UnauthorizedException(String message) {
        super("NAO_AUTENTICADO", message);
    }
}
