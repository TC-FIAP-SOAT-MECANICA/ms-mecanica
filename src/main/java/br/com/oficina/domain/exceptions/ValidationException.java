package br.com.oficina.domain.exceptions;

public class ValidationException extends DomainException {

    public ValidationException(String message) {
        super("VALIDACAO", message);
    }
}
