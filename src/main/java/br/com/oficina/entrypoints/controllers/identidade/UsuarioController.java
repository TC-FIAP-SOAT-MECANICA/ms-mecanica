package br.com.oficina.entrypoints.controllers.identidade;

import br.com.oficina.application.dto.identidade.UsuarioOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.usecase.identidade.CadastrarUsuarioUseCase;
import br.com.oficina.application.usecase.identidade.ListarUsuariosUseCase;
import br.com.oficina.application.usecase.identidade.ObterUsuarioUseCase;
import br.com.oficina.contract.api.UsuariosApi;
import br.com.oficina.contract.model.PaginaUsuariosDto;
import br.com.oficina.contract.model.PerfilUsuarioDto;
import br.com.oficina.contract.model.UsuarioRequestDto;
import br.com.oficina.contract.model.UsuarioResponseDto;
import br.com.oficina.entrypoints.controllers.presenter.identidade.UsuarioPresenter;
import br.com.oficina.infrastructure.dataproviders.config.security.SolicitanteAtual;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
public class UsuarioController implements UsuariosApi {

    private final CadastrarUsuarioUseCase cadastrarUsuario;
    private final ObterUsuarioUseCase obterUsuario;
    private final ListarUsuariosUseCase listarUsuarios;

    public UsuarioController(CadastrarUsuarioUseCase cadastrarUsuario, ObterUsuarioUseCase obterUsuario,
                             ListarUsuariosUseCase listarUsuarios) {
        this.cadastrarUsuario = cadastrarUsuario;
        this.obterUsuario = obterUsuario;
        this.listarUsuarios = listarUsuarios;
    }

    @Override
    public ResponseEntity<UsuarioResponseDto> cadastrarUsuario(UUID idempotencyKey, UsuarioRequestDto request) {
        UsuarioOutput usuario = cadastrarUsuario.execute(UsuarioPresenter.toInput(request));
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuario.id())
                .toUri();
        return ResponseEntity.created(location).body(UsuarioPresenter.toResponse(usuario));
    }

    @Override
    public ResponseEntity<UsuarioResponseDto> obterUsuario(Integer usuarioId) {
        return ResponseEntity.ok(UsuarioPresenter.toResponse(obterUsuario.execute(usuarioId)));
    }

    @Override
    public ResponseEntity<UsuarioResponseDto> obterUsuarioAutenticado() {
        return ResponseEntity.ok(UsuarioPresenter.toResponse(obterUsuario.execute(SolicitanteAtual.get().usuarioId())));
    }

    @Override
    public ResponseEntity<PaginaUsuariosDto> listarUsuarios(Integer page, Integer size, PerfilUsuarioDto perfil) {
        PageResult<UsuarioOutput> pagina = listarUsuarios.execute(perfil == null ? null : perfil.getValue(), null,
                PageQuery.of(page, size));
        return ResponseEntity.ok(UsuarioPresenter.toPagina(pagina));
    }
}
