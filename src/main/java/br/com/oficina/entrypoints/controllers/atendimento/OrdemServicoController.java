package br.com.oficina.entrypoints.controllers.atendimento;

import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.usecase.atendimento.AbrirOrdemServicoUseCase;
import br.com.oficina.application.usecase.atendimento.EntregarVeiculoUseCase;
import br.com.oficina.application.usecase.atendimento.FinalizarOrdemServicoUseCase;
import br.com.oficina.application.usecase.atendimento.IncluirPecaNaOrdemUseCase;
import br.com.oficina.application.usecase.atendimento.IncluirServicoNaOrdemUseCase;
import br.com.oficina.application.usecase.atendimento.ListarOrdensServicoUseCase;
import br.com.oficina.application.usecase.atendimento.ObterOrdemServicoUseCase;
import br.com.oficina.application.usecase.atendimento.RegistrarDiagnosticoUseCase;
import br.com.oficina.application.usecase.atendimento.RemoverPecaDaOrdemUseCase;
import br.com.oficina.application.usecase.atendimento.RemoverServicoDaOrdemUseCase;
import br.com.oficina.contract.api.OrdensServicoApi;
import br.com.oficina.contract.model.AbrirOrdemServicoRequestDto;
import br.com.oficina.contract.model.DiagnosticoRequestDto;
import br.com.oficina.contract.model.ItemPecaRequestDto;
import br.com.oficina.contract.model.ItemServicoRequestDto;
import br.com.oficina.contract.model.OrdemServicoResponseDto;
import br.com.oficina.contract.model.PaginaOrdensServicoDto;
import br.com.oficina.contract.model.StatusOSDto;
import br.com.oficina.entrypoints.controllers.presenter.atendimento.OrdemServicoPresenter;
import br.com.oficina.infrastructure.dataproviders.config.security.SolicitanteAtual;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
public class OrdemServicoController implements OrdensServicoApi {

    private final AbrirOrdemServicoUseCase abrirOrdemServico;
    private final ObterOrdemServicoUseCase obterOrdemServico;
    private final ListarOrdensServicoUseCase listarOrdensServico;
    private final ItensOrdemServicoUseCases itens;
    private final RegistrarDiagnosticoUseCase registrarDiagnostico;
    private final FinalizarOrdemServicoUseCase finalizarOrdemServico;
    private final EntregarVeiculoUseCase entregarVeiculo;

    public OrdemServicoController(AbrirOrdemServicoUseCase abrirOrdemServico,
                                  ObterOrdemServicoUseCase obterOrdemServico,
                                  ListarOrdensServicoUseCase listarOrdensServico,
                                  IncluirServicoNaOrdemUseCase incluirServico,
                                  RemoverServicoDaOrdemUseCase removerServico,
                                  IncluirPecaNaOrdemUseCase incluirPeca,
                                  RemoverPecaDaOrdemUseCase removerPeca,
                                  RegistrarDiagnosticoUseCase registrarDiagnostico,
                                  FinalizarOrdemServicoUseCase finalizarOrdemServico,
                                  EntregarVeiculoUseCase entregarVeiculo) {
        this.abrirOrdemServico = abrirOrdemServico;
        this.obterOrdemServico = obterOrdemServico;
        this.listarOrdensServico = listarOrdensServico;
        this.itens = new ItensOrdemServicoUseCases(incluirServico, removerServico, incluirPeca, removerPeca);
        this.registrarDiagnostico = registrarDiagnostico;
        this.finalizarOrdemServico = finalizarOrdemServico;
        this.entregarVeiculo = entregarVeiculo;
    }

    @Override
    public ResponseEntity<OrdemServicoResponseDto> abrirOrdemServico(UUID idempotencyKey,
                                                                     AbrirOrdemServicoRequestDto request) {
        OrdemServicoOutput ordemServico = abrirOrdemServico.execute(OrdemServicoPresenter.toInput(request));
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ordemServico.id())
                .toUri();
        return ResponseEntity.created(location).body(OrdemServicoPresenter.toResponse(ordemServico));
    }

    @Override
    public ResponseEntity<OrdemServicoResponseDto> obterOrdemServico(Integer osId) {
        return ok(obterOrdemServico.execute(osId, SolicitanteAtual.get()));
    }

    @Override
    public ResponseEntity<PaginaOrdensServicoDto> listarOrdensServico(Integer page, Integer size,
                                                                      Set<StatusOSDto> status, Integer clienteId,
                                                                      Integer veiculoId) {
        List<String> filtroStatus = OrdemServicoPresenter.toFiltroStatus(status);
        PageResult<OrdemServicoOutput> pagina = listarOrdensServico.execute(filtroStatus, clienteId, veiculoId,
                PageQuery.of(page, size), SolicitanteAtual.get());
        return ResponseEntity.ok(OrdemServicoPresenter.toPagina(pagina));
    }

    @Override
    public ResponseEntity<OrdemServicoResponseDto> incluirServicoNaOrdem(Integer osId, ItemServicoRequestDto request) {
        return ok(itens.incluirServico().execute(osId, OrdemServicoPresenter.toInput(request)));
    }

    @Override
    public ResponseEntity<OrdemServicoResponseDto> removerServicoDaOrdem(Integer osId, Integer itemId) {
        return ok(itens.removerServico().execute(osId, itemId));
    }

    @Override
    public ResponseEntity<OrdemServicoResponseDto> incluirPecaNaOrdem(Integer osId, ItemPecaRequestDto request) {
        return ok(itens.incluirPeca().execute(osId, OrdemServicoPresenter.toInput(request)));
    }

    @Override
    public ResponseEntity<OrdemServicoResponseDto> removerPecaDaOrdem(Integer osId, Integer itemId) {
        return ok(itens.removerPeca().execute(osId, itemId));
    }

    @Override
    public ResponseEntity<OrdemServicoResponseDto> registrarDiagnostico(Integer osId, DiagnosticoRequestDto request) {
        return ok(registrarDiagnostico.execute(osId, request.getDiagnosticoTecnico()));
    }

    @Override
    public ResponseEntity<OrdemServicoResponseDto> finalizarOrdemServico(Integer osId) {
        return ok(finalizarOrdemServico.execute(osId));
    }

    @Override
    public ResponseEntity<OrdemServicoResponseDto> entregarVeiculo(Integer osId) {
        return ok(entregarVeiculo.execute(osId));
    }

    private static ResponseEntity<OrdemServicoResponseDto> ok(OrdemServicoOutput ordemServico) {
        return ResponseEntity.ok(OrdemServicoPresenter.toResponse(ordemServico));
    }

    private record ItensOrdemServicoUseCases(IncluirServicoNaOrdemUseCase incluirServico,
                                             RemoverServicoDaOrdemUseCase removerServico,
                                             IncluirPecaNaOrdemUseCase incluirPeca,
                                             RemoverPecaDaOrdemUseCase removerPeca) {
    }
}
