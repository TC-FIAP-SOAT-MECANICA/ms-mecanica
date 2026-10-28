package br.com.oficina.identidade.adapter.web;

import br.com.oficina.contract.api.AuthApi;
import br.com.oficina.contract.model.LoginRequestDto;
import br.com.oficina.contract.model.PerfilUsuarioDto;
import br.com.oficina.contract.model.TokenResponseDto;
import br.com.oficina.identidade.application.dto.TokenOutput;
import br.com.oficina.identidade.application.usecase.AutenticarUsuarioUseCase;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {

    private final AutenticarUsuarioUseCase autenticarUsuario;

    public AuthController(AutenticarUsuarioUseCase autenticarUsuario) {
        this.autenticarUsuario = autenticarUsuario;
    }

    @Override
    public ResponseEntity<TokenResponseDto> autenticarUsuario(LoginRequestDto request) {
        TokenOutput token = autenticarUsuario.execute(request.getEmail(), request.getSenha());
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(new TokenResponseDto()
                        .accessToken(token.accessToken())
                        .tokenType(TokenResponseDto.TokenTypeEnum.BEARER)
                        .expiresIn((int) token.expiresIn())
                        .usuarioId(token.usuarioId())
                        .perfil(PerfilUsuarioDto.fromValue(token.perfil()))
                        .clienteId(token.clienteId()));
    }
}
