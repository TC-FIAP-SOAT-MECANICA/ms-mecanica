package br.com.oficina.shared.domain.exception;

/** Conflito com o estado atual (ex.: CPF/CNPJ já cadastrado). Vira HTTP 409 no {@code ApiExceptionHandler}. */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
