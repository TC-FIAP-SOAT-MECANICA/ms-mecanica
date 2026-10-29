package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.AbrirOrdemServicoInput;
import br.com.oficina.application.dto.atendimento.ItemPecaInput;
import br.com.oficina.application.dto.atendimento.ItemServicoInput;
import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.gateways.atendimento.CadastroGateway;
import br.com.oficina.application.gateways.atendimento.CatalogoGateway;
import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.domain.exceptions.BusinessRuleException;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.atendimento.OrdemServico;
import br.com.oficina.domain.model.atendimento.StatusOrdemServico;
import br.com.oficina.domain.valueobject.Dinheiro;
import br.com.oficina.domain.valueobject.Quantidade;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AbrirOrdemServicoUseCaseImplTest {

    private static final Instant AGORA = Instant.parse("2026-10-25T12:00:00Z");

    private final List<String> movimentos = new ArrayList<>();
    private final CadastroGateway cadastro = new CadastroGateway() {
        @Override
        public boolean clienteExiste(Integer clienteId) {
            return clienteId == 1 || clienteId == 2;
        }

        @Override
        public Optional<Integer> clienteDoVeiculo(Integer veiculoId) {
            return Optional.ofNullable(Map.of(10, 1).get(veiculoId));
        }
    };
    private final CatalogoGateway catalogo = servicoId -> {
        if (servicoId != 100) {
            throw new ResourceNotFoundException("Serviço", servicoId);
        }
        return Dinheiro.de("120.00");
    };
    private final EstoqueGateway estoque = new EstoqueGateway() {
        @Override
        public Dinheiro baixar(Integer pecaInsumoId, Quantidade quantidade) {
            movimentos.add("baixa " + pecaInsumoId + " " + quantidade);
            return Dinheiro.de("35.50");
        }

        @Override
        public void devolver(Integer pecaInsumoId, Quantidade quantidade) {
            movimentos.add("devolve " + pecaInsumoId + " " + quantidade);
        }
    };
    private final InMemoryOrdemServicoRepository repository = new InMemoryOrdemServicoRepository();
    private final AbrirOrdemServicoUseCaseImpl useCase = new AbrirOrdemServicoUseCaseImpl(repository, cadastro,
            catalogo, estoque, new DirectUnitOfWork(), Clock.fixed(AGORA, ZoneOffset.UTC));

    @Test
    void abreOrdemComPrecosDoCatalogoEDoEstoque() {
        OrdemServicoOutput os = useCase.execute(new AbrirOrdemServicoInput(1, 10, "Barulho",
                List.of(new ItemServicoInput(100, null)), List.of(new ItemPecaInput(200, new BigDecimal("2")))));

        assertThat(os.status()).isEqualTo(StatusOrdemServico.ABERTA.name());
        assertThat(os.valorTotal()).isEqualTo("191.00");
        assertThat(os.dataAbertura()).isEqualTo(AGORA);
        assertThat(movimentos).containsExactly("baixa 200 2.00");
    }

    @Test
    void usaPrecoFinalInformado() {
        OrdemServicoOutput os = useCase.execute(new AbrirOrdemServicoInput(1, 10, null,
                List.of(new ItemServicoInput(100, "99.90")), null));

        assertThat(os.servicos().getFirst().precoFinal()).isEqualTo("99.90");
    }

    @Test
    void recusaVeiculoDeOutroCliente() {
        assertThatThrownBy(() -> useCase.execute(new AbrirOrdemServicoInput(2, 10, null, null, null)))
                .isInstanceOf(BusinessRuleException.class)
                .extracting("code").isEqualTo("VEICULO_NAO_PERTENCE_AO_CLIENTE");
        assertThat(repository.salvas).isEmpty();
    }

    @Test
    void recusaClienteInexistente() {
        assertThatThrownBy(() -> useCase.execute(new AbrirOrdemServicoInput(9, 10, null, null, null)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private static class DirectUnitOfWork implements UnitOfWork {

        @Override
        public <T> T execute(Supplier<T> work) {
            return work.get();
        }
    }

    private static class InMemoryOrdemServicoRepository implements OrdemServicoRepository {

        private final List<OrdemServico> salvas = new ArrayList<>();

        @Override
        public OrdemServico save(OrdemServico os) {
            OrdemServico salva = OrdemServico.restaurar(salvas.size() + 1, os.clienteId(), os.veiculoId(),
                    os.status(), os.descricaoProblema(), os.diagnosticoTecnico(), os.dataAbertura(),
                    os.dataConclusao(), os.servicos(), os.pecas());
            salvas.add(salva);
            return salva;
        }

        @Override
        public Optional<OrdemServico> findById(Integer id) {
            return salvas.stream().filter(os -> os.id().equals(id)).findFirst();
        }

        @Override
        public PageResult<OrdemServico> findAll(List<StatusOrdemServico> status, Integer clienteId,
                                                Integer veiculoId, PageQuery pageQuery) {
            return new PageResult<>(List.copyOf(salvas), 0, salvas.size(), salvas.size(), 1);
        }
    }
}
