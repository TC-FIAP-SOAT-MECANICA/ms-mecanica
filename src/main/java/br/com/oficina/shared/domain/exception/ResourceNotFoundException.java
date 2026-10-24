package br.com.oficina.shared.domain.exception;

import java.util.UUID;

/** Registro inexistente. Vira HTTP 404 no {@code ApiExceptionHandler}. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, UUID id) {
        super(resource + " não encontrado: " + id);
    }
}
