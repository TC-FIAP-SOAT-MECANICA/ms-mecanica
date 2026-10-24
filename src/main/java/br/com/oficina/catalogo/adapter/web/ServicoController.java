package br.com.oficina.catalogo.adapter.web;

import br.com.oficina.catalogo.application.dto.CadastrarServicoInput;
import br.com.oficina.catalogo.application.dto.ServicoOutput;
import br.com.oficina.catalogo.application.usecase.CadastrarServicoUseCase;
import br.com.oficina.contract.api.ServicosApi;
import br.com.oficina.contract.model.ServicoRequestDto;
import br.com.oficina.contract.model.ServicoResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

/**
 * Adapter de entrada: traduz HTTP ⇄ caso de uso. Não tem regra de negócio.
 *
 * <p>API First: rota, validação e DTOs vêm da interface {@link ServicosApi}, gerada a partir do contrato
 * ({@code oficina-mecanica-contract.yaml}). Aqui só se sobrescreve o que já está implementado; as demais
 * operações de {@code /servicos} respondem 501 pelo método default da interface.
 *
 * <p>Fluxo: JSON → {@link ServicoRequestDto} → caso de uso → {@link ServicoOutput} → {@link ServicoResponseDto}.
 */
@RestController
public class ServicoController implements ServicosApi {

    private final CadastrarServicoUseCase cadastrarServico;

    public ServicoController(CadastrarServicoUseCase cadastrarServico) {
        this.cadastrarServico = cadastrarServico;
    }

    @Override
    public ResponseEntity<ServicoResponseDto> cadastrarServico(UUID idempotencyKey, ServicoRequestDto request) {
        // TODO: usar a Idempotency-Key para não cadastrar duas vezes em reenvios.
        var input = new CadastrarServicoInput(request.getNome(), request.getDescricao(), request.getPreco(),
                request.getTempoEstimadoMinutos());
        ServicoOutput servico = cadastrarServico.execute(input);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(servico.id())
                .toUri();
        return ResponseEntity.created(location).body(toResponse(servico));
    }

    private static ServicoResponseDto toResponse(ServicoOutput servico) {
        return new ServicoResponseDto()
                .id(servico.id())
                .nome(servico.nome())
                .descricao(servico.descricao())
                .preco(servico.preco())
                .tempoEstimadoMinutos(servico.tempoEstimadoMinutos())
                .ativo(servico.ativo());
    }
}
