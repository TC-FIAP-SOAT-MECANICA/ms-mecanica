package br.com.oficina.catalogo.adapter.web;

import br.com.oficina.catalogo.application.dto.ServicoInput;
import br.com.oficina.catalogo.application.dto.ServicoOutput;
import br.com.oficina.catalogo.application.usecase.AtualizarServicoUseCase;
import br.com.oficina.catalogo.application.usecase.CadastrarServicoUseCase;
import br.com.oficina.catalogo.application.usecase.ListarServicosUseCase;
import br.com.oficina.catalogo.application.usecase.ObterServicoUseCase;
import br.com.oficina.catalogo.application.usecase.RemoverServicoUseCase;
import br.com.oficina.contract.api.ServicosApi;
import br.com.oficina.contract.model.PaginaServicosDto;
import br.com.oficina.contract.model.ServicoRequestDto;
import br.com.oficina.contract.model.ServicoResponseDto;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
public class ServicoController implements ServicosApi {

    private final CadastrarServicoUseCase cadastrarServico;
    private final ObterServicoUseCase obterServico;
    private final ListarServicosUseCase listarServicos;
    private final AtualizarServicoUseCase atualizarServico;
    private final RemoverServicoUseCase removerServico;

    public ServicoController(CadastrarServicoUseCase cadastrarServico, ObterServicoUseCase obterServico,
                             ListarServicosUseCase listarServicos, AtualizarServicoUseCase atualizarServico,
                             RemoverServicoUseCase removerServico) {
        this.cadastrarServico = cadastrarServico;
        this.obterServico = obterServico;
        this.listarServicos = listarServicos;
        this.atualizarServico = atualizarServico;
        this.removerServico = removerServico;
    }

    @Override
    public ResponseEntity<ServicoResponseDto> cadastrarServico(UUID idempotencyKey, ServicoRequestDto request) {
        ServicoOutput servico = cadastrarServico.execute(toInput(request));
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(servico.id())
                .toUri();
        return ResponseEntity.created(location).body(toResponse(servico));
    }

    @Override
    public ResponseEntity<ServicoResponseDto> obterServico(Integer servicoId) {
        return ResponseEntity.ok(toResponse(obterServico.execute(servicoId)));
    }

    @Override
    public ResponseEntity<PaginaServicosDto> listarServicos(Integer page, Integer size, String nome) {
        PageResult<ServicoOutput> pagina = listarServicos.execute(nome, PageQuery.of(page, size));
        return ResponseEntity.ok(new PaginaServicosDto()
                .page(pagina.page())
                .size(pagina.size())
                .totalElements(pagina.totalElements())
                .totalPages(pagina.totalPages())
                .content(pagina.content().stream().map(ServicoController::toResponse).toList()));
    }

    @Override
    public ResponseEntity<ServicoResponseDto> atualizarServico(Integer servicoId, ServicoRequestDto request) {
        return ResponseEntity.ok(toResponse(atualizarServico.execute(servicoId, toInput(request))));
    }

    @Override
    public ResponseEntity<Void> removerServico(Integer servicoId) {
        removerServico.execute(servicoId);
        return ResponseEntity.noContent().build();
    }

    private static ServicoInput toInput(ServicoRequestDto request) {
        return new ServicoInput(request.getNome(), request.getDescricao(), request.getPrecoBase());
    }

    private static ServicoResponseDto toResponse(ServicoOutput servico) {
        return new ServicoResponseDto()
                .id(servico.id())
                .nome(servico.nome())
                .descricao(servico.descricao())
                .precoBase(servico.precoBase());
    }
}
