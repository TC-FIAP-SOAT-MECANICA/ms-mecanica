package br.com.oficina.shared.domain.exception;

public class ResourceNotFoundException extends DomainException {

    public ResourceNotFoundException(String resource, Object id) {
        super("RECURSO_NAO_ENCONTRADO", resource + " não encontrado: " + id);
    }
}
