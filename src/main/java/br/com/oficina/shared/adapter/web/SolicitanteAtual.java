package br.com.oficina.shared.adapter.web;

import br.com.oficina.shared.application.dto.Solicitante;
import br.com.oficina.shared.application.dto.Solicitante.Papel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public final class SolicitanteAtual {

    public static final String CLAIM_PERFIL = "perfil";
    public static final String CLAIM_CLIENTE_ID = "clienteId";

    private SolicitanteAtual() {
    }

    public static Solicitante get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            return Solicitante.anonimo();
        }
        Number clienteId = jwt.getClaim(CLAIM_CLIENTE_ID);
        return new Solicitante(Integer.valueOf(jwt.getSubject()), Papel.valueOf(jwt.getClaimAsString(CLAIM_PERFIL)),
                clienteId == null ? null : clienteId.intValue());
    }
}
