package br.com.oficina.entrypoints.controllers.identidade;

import br.com.oficina.application.dto.identidade.TokenOutput;
import br.com.oficina.application.usecase.identidade.AutenticarUsuarioUseCase;
import br.com.oficina.contract.api.AuthApi;
import br.com.oficina.contract.model.LoginRequestDto;
import br.com.oficina.contract.model.TokenResponseDto;
import br.com.oficina.entrypoints.controllers.presenter.identidade.UsuarioPresenter;
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
                .body(UsuarioPresenter.toResponse(token));
    }
}
