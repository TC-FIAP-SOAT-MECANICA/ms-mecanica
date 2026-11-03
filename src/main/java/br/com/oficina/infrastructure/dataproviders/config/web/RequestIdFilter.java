package br.com.oficina.infrastructure.dataproviders.config.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdFilter extends OncePerRequestFilter {

    public static final String HEADER = "X-Request-Id";
    public static final String MDC_KEY = "requestId";

    private static final Logger log = LoggerFactory.getLogger(RequestIdFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String requestId = normalizar(request.getHeader(HEADER));
        MDC.put(MDC_KEY, requestId);
        response.setHeader(HEADER, requestId);
        long inicio = System.nanoTime();
        try {
            chain.doFilter(request, response);
        } finally {
            registrarChamada(request, response, inicio);
            MDC.remove(MDC_KEY);
        }
    }

    private static void registrarChamada(HttpServletRequest request, HttpServletResponse response, long inicio) {
        String caminho = request.getRequestURI();
        if (caminho.contains("/actuator")) {
            return;
        }
        long duracaoMs = (System.nanoTime() - inicio) / 1_000_000;
        log.info("{} {} -> {} ({} ms)", request.getMethod(), caminho, response.getStatus(), duracaoMs);
    }

    private static String normalizar(String valor) {
        if (valor != null) {
            try {
                return UUID.fromString(valor).toString();
            } catch (IllegalArgumentException ignored) {
                return UUID.randomUUID().toString();
            }
        }
        return UUID.randomUUID().toString();
    }
}
