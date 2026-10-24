package br.com.oficina.shared.adapter.web;

import br.com.oficina.shared.domain.exception.ConflictException;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduz exceções para respostas HTTP no formato RFC 9457 ({@code application/problem+json}).
 *
 * <p>O domínio só lança exceções Java; é aqui que se decide o status HTTP. Erros de validação do JSON
 * ({@code @Valid}) já são tratados pelo Spring com {@code spring.mvc.problemdetails.enabled=true}.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail regraViolada(IllegalArgumentException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    ProblemDetail conflito(ConflictException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail naoEncontrado(ResourceNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
