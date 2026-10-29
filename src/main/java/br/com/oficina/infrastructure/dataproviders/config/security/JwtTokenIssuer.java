package br.com.oficina.infrastructure.dataproviders.config.security;

import br.com.oficina.application.gateways.identidade.TokenIssuer;
import br.com.oficina.domain.model.identidade.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Component
class JwtTokenIssuer implements TokenIssuer {

    private final JwtEncoder encoder;
    private final Clock clock;
    private final Duration validade;
    private final String emissor;

    JwtTokenIssuer(JwtEncoder encoder, Clock clock,
                   @Value("${app.security.jwt.expiracao-minutos}") long expiracaoMinutos,
                   @Value("${app.security.jwt.emissor}") String emissor) {
        this.encoder = encoder;
        this.clock = clock;
        this.validade = Duration.ofMinutes(expiracaoMinutos);
        this.emissor = emissor;
    }

    @Override
    public TokenEmitido emitir(Usuario usuario, Integer clienteId) {
        Instant agora = clock.instant();
        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .issuer(emissor)
                .subject(usuario.id().toString())
                .issuedAt(agora)
                .expiresAt(agora.plus(validade))
                .claim(SolicitanteAtual.CLAIM_PERFIL, usuario.perfil().name());
        if (clienteId != null) {
            claims.claim(SolicitanteAtual.CLAIM_CLIENTE_ID, clienteId);
        }
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = encoder.encode(JwtEncoderParameters.from(header, claims.build())).getTokenValue();
        return new TokenEmitido(token, validade.toSeconds());
    }
}
