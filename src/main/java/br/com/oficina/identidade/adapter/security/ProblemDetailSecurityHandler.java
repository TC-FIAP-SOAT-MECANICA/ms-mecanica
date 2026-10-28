package br.com.oficina.identidade.adapter.security;

import br.com.oficina.shared.adapter.web.ApiExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.URI;

class ProblemDetailSecurityHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final JsonMapper jsonMapper;

    ProblemDetailSecurityHandler(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException {
        escrever(request, response, HttpStatus.UNAUTHORIZED, "NAO_AUTENTICADO",
                "Token ausente, inválido ou expirado");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException {
        escrever(request, response, HttpStatus.FORBIDDEN, "ACESSO_NEGADO",
                "Seu perfil não tem permissão para esta operação");
    }

    private void escrever(HttpServletRequest request, HttpServletResponse response, HttpStatus status,
                          String codigo, String detalhe) throws IOException {
        ProblemDetail problem = ApiExceptionHandler.problem(status, codigo, detalhe);
        problem.setInstance(URI.create(request.getRequestURI()));
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        jsonMapper.writeValue(response.getOutputStream(), problem);
    }
}
