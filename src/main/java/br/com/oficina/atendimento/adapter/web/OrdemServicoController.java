package br.com.oficina.atendimento.adapter.web;

import br.com.oficina.atendimento.application.dto.AbrirOrdemServicoInput;
import br.com.oficina.atendimento.application.dto.ItemPecaInput;
import br.com.oficina.atendimento.application.dto.ItemServicoInput;
import br.com.oficina.atendimento.application.dto.OrdemServicoOutput;
import br.com.oficina.atendimento.application.usecase.AbrirOrdemServicoUseCase;
import br.com.oficina.atendimento.application.usecase.EntregarVeiculoUseCase;
import br.com.oficina.atendimento.application.usecase.FinalizarOrdemServicoUseCase;
import br.com.oficina.atendimento.application.usecase.IncluirPecaNaOrdemUseCase;
import br.com.oficina.atendimento.application.usecase.IncluirServicoNaOrdemUseCase;
import br.com.oficina.atendimento.application.usecase.ListarOrdensServicoUseCase;
import br.com.oficina.atendimento.application.usecase.ObterOrdemServicoUseCase;
import br.com.oficina.atendimento.application.usecase.RegistrarDiagnosticoUseCase;
import br.com.oficina.atendimento.application.usecase.RemoverPecaDaOrdemUseCase;
import br.com.oficina.atendimento.application.usecase.RemoverServicoDaOrdemUseCase;
import br.com.oficina.contract.api.OrdensServicoApi;
import br.com.oficina.contract.model.AbrirOrdemServicoRequestDto;
import br.com.oficina.contract.model.DiagnosticoRequestDto;
import br.com.oficina.contract.model.ItemPecaRequestDto;
import br.com.oficina.contract.model.ItemServicoRequestDto;
import br.com.oficina.contract.model.OrdemServicoResponseDto;
import br.com.oficina.contract.model.PaginaOrdensServicoDto;
import br.com.oficina.contract.model.StatusOSDto;
import br.com.oficina.shared.adapter.web.SolicitanteAtual;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
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
        OrdemServicoOutput ordemServico = abrirOrdemServico.execute(new AbrirOrdemServicoInput(
                request.getClienteId(), request.getVeiculoId(), request.getDescricaoProblema(),
                request.getServicos().stream().map(OrdemServicoController::toInput).toList(),
                request.getPecas().stream().map(OrdemServicoController::toInput).toList()));
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ordemServico.id())
                .toUri();
        return ResponseEntity.created(location).body(OrdemServicoMapper.toResponse(ordemServico));
    }

    @Override
    public ResponseEntity<OrdemServicoResponseDto> obterOrdemServico(Integer osId) {
        return ok(obterOrdemServico.execute(osId, SolicitanteAtual.get()));
    }

    @Override
    public ResponseEntity<PaginaOrdensServicoDto> listarOrdensServico(Integer page, Integer size,
                                                                      Set<StatusOSDto> status, Integer clienteId,
                                                                      Integer veiculoId) {
        List<String> filtroStatus = status == null ? List.of() : status.stream().map(StatusOSDto::getValue).toList();
        PageResult<OrdemServicoOutput> pagina = listarOrdensServico.execute(filtroStatus, clienteId, veiculoId,
                PageQuery.of(page, size), SolicitanteAtual.get());
        return ResponseEntity.ok(new PaginaOrdensServicoDto()
                .page(pagina.page())
                .size(pagina.size())
                .totalElements(pagina.totalElements())
                .totalPages(pagina.totalPages())
                .content(pagina.content().stream().map(OrdemServicoMapper::toResponse).toList()));
    }

    @Override
    public ResponseEntity<OrdemServicoResponseDto> incluirServicoNaOrdem(Integer osId, ItemServicoRequestDto request) {
        return ok(itens.incluirServico().execute(osId, toInput(request)));
    }

    @Override
    public ResponseEntity<OrdemServicoResponseDto> removerServicoDaOrdem(Integer osId, Integer itemId) {
        return ok(itens.removerServico().execute(osId, itemId));
    }

    @Override
    public ResponseEntity<OrdemServicoResponseDto> incluirPecaNaOrdem(Integer osId, ItemPecaRequestDto request) {
        return ok(itens.incluirPeca().execute(osId, toInput(request)));
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
        return ResponseEntity.ok(OrdemServicoMapper.toResponse(ordemServico));
    }

    private static ItemServicoInput toInput(ItemServicoRequestDto request) {
        return new ItemServicoInput(request.getServicoId(), request.getPrecoFinal());
    }

    private static ItemPecaInput toInput(ItemPecaRequestDto request) {
        return new ItemPecaInput(request.getPecaInsumoId(), request.getQuantidade());
    }

    private record ItensOrdemServicoUseCases(IncluirServicoNaOrdemUseCase incluirServico,
                                             RemoverServicoDaOrdemUseCase removerServico,
                                             IncluirPecaNaOrdemUseCase incluirPeca,
                                             RemoverPecaDaOrdemUseCase removerPeca) {
    }
}
