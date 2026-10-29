package br.com.oficina.entrypoints.controllers.presenter.identidade;

import br.com.oficina.application.dto.identidade.CadastrarUsuarioInput;
import br.com.oficina.application.dto.identidade.TokenOutput;
import br.com.oficina.application.dto.identidade.UsuarioOutput;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.contract.model.PaginaUsuariosDto;
import br.com.oficina.contract.model.PerfilUsuarioDto;
import br.com.oficina.contract.model.TokenResponseDto;
import br.com.oficina.contract.model.UsuarioRequestDto;
import br.com.oficina.contract.model.UsuarioResponseDto;

import java.time.ZoneOffset;

public final class UsuarioPresenter {

    private UsuarioPresenter() {
    }

    public static CadastrarUsuarioInput toInput(UsuarioRequestDto request) {
        return new CadastrarUsuarioInput(request.getNome(), request.getEmail(), request.getTelefone(),
                request.getSenha(), request.getPerfil().getValue());
    }

    public static UsuarioResponseDto toResponse(UsuarioOutput usuario) {
        return new UsuarioResponseDto()
                .id(usuario.id())
                .nome(usuario.nome())
                .email(usuario.email())
                .telefone(usuario.telefone())
                .perfil(PerfilUsuarioDto.fromValue(usuario.perfil()))
                .criadoEm(usuario.criadoEm() == null ? null : usuario.criadoEm().atOffset(ZoneOffset.UTC));
    }

    public static PaginaUsuariosDto toPagina(PageResult<UsuarioOutput> pagina) {
        return new PaginaUsuariosDto()
                .page(pagina.page())
                .size(pagina.size())
                .totalElements(pagina.totalElements())
                .totalPages(pagina.totalPages())
                .content(pagina.content().stream().map(UsuarioPresenter::toResponse).toList());
    }

    public static TokenResponseDto toResponse(TokenOutput token) {
        return new TokenResponseDto()
                .accessToken(token.accessToken())
                .tokenType(TokenResponseDto.TokenTypeEnum.BEARER)
                .expiresIn((int) token.expiresIn())
                .usuarioId(token.usuarioId())
                .perfil(PerfilUsuarioDto.fromValue(token.perfil()))
                .clienteId(token.clienteId());
    }
}
