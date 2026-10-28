package br.com.oficina.identidade.adapter.web;

import br.com.oficina.contract.api.UsuariosApi;
import br.com.oficina.contract.model.PaginaUsuariosDto;
import br.com.oficina.contract.model.PerfilUsuarioDto;
import br.com.oficina.contract.model.UsuarioRequestDto;
import br.com.oficina.contract.model.UsuarioResponseDto;
import br.com.oficina.identidade.application.dto.CadastrarUsuarioInput;
import br.com.oficina.identidade.application.dto.UsuarioOutput;
import br.com.oficina.identidade.application.usecase.CadastrarUsuarioUseCase;
import br.com.oficina.identidade.application.usecase.ListarUsuariosUseCase;
import br.com.oficina.identidade.application.usecase.ObterUsuarioUseCase;
import br.com.oficina.shared.adapter.web.SolicitanteAtual;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.ZoneOffset;
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
        UsuarioOutput usuario = cadastrarUsuario.execute(new CadastrarUsuarioInput(request.getNome(),
                request.getEmail(), request.getTelefone(), request.getSenha(), request.getPerfil().getValue()));
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuario.id())
                .toUri();
        return ResponseEntity.created(location).body(toResponse(usuario));
    }

    @Override
    public ResponseEntity<UsuarioResponseDto> obterUsuario(Integer usuarioId) {
        return ResponseEntity.ok(toResponse(obterUsuario.execute(usuarioId)));
    }

    @Override
    public ResponseEntity<UsuarioResponseDto> obterUsuarioAutenticado() {
        return ResponseEntity.ok(toResponse(obterUsuario.execute(SolicitanteAtual.get().usuarioId())));
    }

    @Override
    public ResponseEntity<PaginaUsuariosDto> listarUsuarios(Integer page, Integer size, PerfilUsuarioDto perfil) {
        PageResult<UsuarioOutput> pagina = listarUsuarios.execute(perfil == null ? null : perfil.getValue(),
                PageQuery.of(page, size));
        return ResponseEntity.ok(new PaginaUsuariosDto()
                .page(pagina.page())
                .size(pagina.size())
                .totalElements(pagina.totalElements())
                .totalPages(pagina.totalPages())
                .content(pagina.content().stream().map(UsuarioController::toResponse).toList()));
    }

    private static UsuarioResponseDto toResponse(UsuarioOutput usuario) {
        return new UsuarioResponseDto()
                .id(usuario.id())
                .nome(usuario.nome())
                .email(usuario.email())
                .telefone(usuario.telefone())
                .perfil(PerfilUsuarioDto.fromValue(usuario.perfil()))
                .criadoEm(usuario.criadoEm() == null ? null : usuario.criadoEm().atOffset(ZoneOffset.UTC));
    }
}
