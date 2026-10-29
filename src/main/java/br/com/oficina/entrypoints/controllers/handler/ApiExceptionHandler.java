package br.com.oficina.entrypoints.controllers.handler;

import br.com.oficina.application.exceptions.ApplicationException;
import br.com.oficina.application.exceptions.ForbiddenException;
import br.com.oficina.application.exceptions.UnauthorizedException;
import br.com.oficina.domain.exceptions.BusinessRuleException;
import br.com.oficina.domain.exceptions.ConflictException;
import br.com.oficina.domain.exceptions.DomainException;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.exceptions.ValidationException;
import br.com.oficina.infrastructure.dataproviders.config.web.RequestIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail invalidArgument(IllegalArgumentException ex) {
        return problem(HttpStatus.BAD_REQUEST, "VALIDACAO", ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    ProblemDetail validation(ValidationException ex) {
        return problem(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(UnauthorizedException.class)
    ProblemDetail unauthorized(UnauthorizedException ex) {
        return problem(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(ForbiddenException.class)
    ProblemDetail forbidden(ForbiddenException ex) {
        return problem(HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail notFound(ResourceNotFoundException ex) {
        return problem(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(ConflictException.class)
    ProblemDetail conflict(ConflictException ex) {
        return problem(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(BusinessRuleException.class)
    ProblemDetail businessRule(BusinessRuleException ex) {
        return problem(HttpStatus.UNPROCESSABLE_CONTENT, ex);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail dataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Violação de integridade: {}", ex.getMostSpecificCause().getMessage());
        return problem(HttpStatus.CONFLICT, "REGISTRO_EM_USO",
                "Operação viola uma restrição do banco (registro em uso ou duplicado)");
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        ResponseEntity<Object> response = super.handleExceptionInternal(ex, body, headers, statusCode, request);
        if (response != null && response.getBody() instanceof ProblemDetail problemDetail) {
            problemDetail.setProperty("codigo", codeFor(statusCode));
            problemDetail.setProperty("traceId", MDC.get(RequestIdFilter.MDC_KEY));
        }
        return response;
    }

    private static String codeFor(HttpStatusCode statusCode) {
        return switch (statusCode.value()) {
            case 401 -> "NAO_AUTENTICADO";
            case 403 -> "ACESSO_NEGADO";
            case 404 -> "RECURSO_NAO_ENCONTRADO";
            case 501 -> "NAO_IMPLEMENTADO";
            default -> statusCode.is5xxServerError() ? "ERRO_INTERNO" : "VALIDACAO";
        };
    }

    private static ProblemDetail problem(HttpStatus status, DomainException ex) {
        return problem(status, ex.code(), ex.getMessage());
    }

    private static ProblemDetail problem(HttpStatus status, ApplicationException ex) {
        return problem(status, ex.code(), ex.getMessage());
    }

    public static ProblemDetail problem(HttpStatus status, String code, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setProperty("codigo", code);
        problemDetail.setProperty("traceId", MDC.get(RequestIdFilter.MDC_KEY));
        return problemDetail;
    }
}
