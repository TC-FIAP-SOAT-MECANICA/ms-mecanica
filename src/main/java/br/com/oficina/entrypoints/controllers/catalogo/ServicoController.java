package br.com.oficina.entrypoints.controllers.catalogo;

import br.com.oficina.application.dto.catalogo.ServicoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.usecase.catalogo.AtualizarServicoUseCase;
import br.com.oficina.application.usecase.catalogo.CadastrarServicoUseCase;
import br.com.oficina.application.usecase.catalogo.ListarServicosUseCase;
import br.com.oficina.application.usecase.catalogo.ObterServicoUseCase;
import br.com.oficina.application.usecase.catalogo.RemoverServicoUseCase;
import br.com.oficina.contract.api.ServicosApi;
import br.com.oficina.contract.model.PaginaServicosDto;
import br.com.oficina.contract.model.ServicoRequestDto;
import br.com.oficina.contract.model.ServicoResponseDto;
import br.com.oficina.entrypoints.controllers.presenter.catalogo.ServicoPresenter;
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
        ServicoOutput servico = cadastrarServico.execute(ServicoPresenter.toInput(request));
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(servico.id())
                .toUri();
        return ResponseEntity.created(location).body(ServicoPresenter.toResponse(servico));
    }

    @Override
    public ResponseEntity<ServicoResponseDto> obterServico(Integer servicoId) {
        return ResponseEntity.ok(ServicoPresenter.toResponse(obterServico.execute(servicoId)));
    }

    @Override
    public ResponseEntity<PaginaServicosDto> listarServicos(Integer page, Integer size, String nome) {
        PageResult<ServicoOutput> pagina = listarServicos.execute(nome, PageQuery.of(page, size));
        return ResponseEntity.ok(ServicoPresenter.toPagina(pagina));
    }

    @Override
    public ResponseEntity<ServicoResponseDto> atualizarServico(Integer servicoId, ServicoRequestDto request) {
        return ResponseEntity.ok(ServicoPresenter.toResponse(atualizarServico.execute(servicoId, ServicoPresenter.toInput(request))));
    }

    @Override
    public ResponseEntity<Void> removerServico(Integer servicoId) {
        removerServico.execute(servicoId);
        return ResponseEntity.noContent().build();
    }
}
