package br.com.oficina.infrastructure.dataproviders.config.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@Import(ProblemDetailSecurityHandler.class)
public class SecurityConfig {

    private static final String ADMIN = "ADMIN";
    private static final String MECANICO = "MECANICO";
    private static final String CLIENTE = "CLIENTE";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, ProblemDetailSecurityHandler problemHandler)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(problemHandler)
                        .accessDeniedHandler(problemHandler))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(problemHandler)
                        .accessDeniedHandler(problemHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health/**", "/actuator/info", "/actuator/prometheus",
                                "/oficina-mecanica-contract.yaml", "/h2-console/**", "/error").permitAll()
                        .requestMatchers(POST, "/auth/login", "/clientes").permitAll()

                        .requestMatchers(GET, "/usuarios/me").hasAnyRole(ADMIN, MECANICO, CLIENTE)
                        .requestMatchers("/usuarios", "/usuarios/**").hasRole(ADMIN)

                        .requestMatchers(GET, "/clientes").hasAnyRole(ADMIN, MECANICO)
                        .requestMatchers(POST, "/clientes/pesquisa").hasAnyRole(ADMIN, MECANICO)
                        .requestMatchers(DELETE, "/clientes/*").hasRole(ADMIN)
                        .requestMatchers(GET, "/clientes/*").hasAnyRole(ADMIN, MECANICO, CLIENTE)
                        .requestMatchers(PUT, "/clientes/*").hasAnyRole(ADMIN, MECANICO, CLIENTE)

                        .requestMatchers(DELETE, "/veiculos/*").hasRole(ADMIN)
                        .requestMatchers("/veiculos", "/veiculos/*").hasAnyRole(ADMIN, MECANICO, CLIENTE)

                        .requestMatchers(GET, "/servicos", "/servicos/*").hasAnyRole(ADMIN, MECANICO, CLIENTE)
                        .requestMatchers("/servicos", "/servicos/*").hasRole(ADMIN)

                        .requestMatchers(DELETE, "/pecas/*").hasRole(ADMIN)
                        .requestMatchers("/pecas", "/pecas/**").hasAnyRole(ADMIN, MECANICO)

                        .requestMatchers(GET, "/ordens-servico", "/ordens-servico/*")
                        .hasAnyRole(ADMIN, MECANICO, CLIENTE)
                        .requestMatchers("/ordens-servico", "/ordens-servico/**").hasAnyRole(ADMIN, MECANICO)

                        .requestMatchers("/metricas/**", "/actuator/**").hasRole(ADMIN)
                        .anyRequest().authenticated());
        return http.build();
    }

    @Bean
    JwtEncoder jwtEncoder(@Value("${app.security.jwt.segredo}") String segredo) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(chave(segredo)));
    }

    @Bean
    JwtDecoder jwtDecoder(@Value("${app.security.jwt.segredo}") String segredo) {
        return NimbusJwtDecoder.withSecretKey(chave(segredo)).macAlgorithm(MacAlgorithm.HS256).build();
    }

    private static JwtAuthenticationConverter jwtAuthenticationConverter() {
        var authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName("perfil");
        authorities.setAuthorityPrefix("ROLE_");
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return converter;
    }

    private static SecretKey chave(String segredoBase64) {
        byte[] bytes = Base64.getDecoder().decode(segredoBase64);
        if (bytes.length < 32) {
            throw new IllegalStateException("app.security.jwt.segredo deve ter ao menos 32 bytes em Base64");
        }
        return new SecretKeySpec(bytes, "HmacSHA256");
    }
}
