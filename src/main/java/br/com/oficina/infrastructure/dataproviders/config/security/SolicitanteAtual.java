package br.com.oficina.infrastructure.dataproviders.config.security;

import br.com.oficina.application.dto.shared.Solicitante;
import br.com.oficina.application.dto.shared.Solicitante.Papel;
import br.com.oficina.application.exceptions.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public final class SolicitanteAtual {

    public static final String CLAIM_PERFIL = "perfil";
    public static final String CLAIM_CLIENTE_ID = "clienteId";

    private SolicitanteAtual() {
    }

    public static Solicitante get() {
        return opcional().orElseThrow(() -> new UnauthorizedException("Autenticação obrigatória"));
    }

    public static Optional<Solicitante> opcional() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            return Optional.empty();
        }
        Number clienteId = jwt.getClaim(CLAIM_CLIENTE_ID);
        return Optional.of(new Solicitante(Integer.valueOf(jwt.getSubject()),
                Papel.valueOf(jwt.getClaimAsString(CLAIM_PERFIL)), clienteId == null ? null : clienteId.intValue()));
    }
}
