package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.TempoMedioExecucaoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.domain.exceptions.ValidationException;
import br.com.oficina.domain.model.atendimento.OrdemServico;
import br.com.oficina.domain.model.atendimento.StatusOrdemServico;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalcularTempoMedioExecucaoUseCaseImplTest {

    private static final LocalDate INICIO = LocalDate.of(2026, 11, 1);
    private static final LocalDate FIM = LocalDate.of(2026, 11, 30);

    private final TemposDeExecucao repository = new TemposDeExecucao();
    private final CalcularTempoMedioExecucaoUseCaseImpl useCase = new CalcularTempoMedioExecucaoUseCaseImpl(repository);

    @Test
    void calculaAMediaEmMinutosComUmaCasaDecimal() {
        repository.tempos = List.of(Duration.ofMinutes(90), Duration.ofMinutes(120), Duration.ofMinutes(196));

        TempoMedioExecucaoOutput metrica = useCase.execute(INICIO, FIM);

        assertThat(metrica.periodoInicio()).isEqualTo(INICIO);
        assertThat(metrica.periodoFim()).isEqualTo(FIM);
        assertThat(metrica.totalOrdensConsideradas()).isEqualTo(3);
        assertThat(metrica.tempoMedioExecucaoMinutos()).isEqualByComparingTo("135.3");
    }

    @Test
    void periodoSemOrdensConcluidasNaoTemMedia() {
        TempoMedioExecucaoOutput metrica = useCase.execute(INICIO, FIM);

        assertThat(metrica.totalOrdensConsideradas()).isZero();
        assertThat(metrica.tempoMedioExecucaoMinutos()).isNull();
    }

    @Test
    void periodoCobreOsDiasInteirosNoHorarioDeBrasilia() {
        useCase.execute(INICIO, FIM);

        assertThat(repository.consultas).containsExactly(
                List.of(Instant.parse("2026-11-01T03:00:00Z"), Instant.parse("2026-12-01T03:00:00Z")));
    }

    @Test
    void aceitaUmPeriodoDeUmDia() {
        assertThat(useCase.execute(INICIO, INICIO).totalOrdensConsideradas()).isZero();
    }

    @Test
    void aceitaAte366Dias() {
        assertThat(useCase.execute(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31))).isNotNull();

        assertThatThrownBy(() -> useCase.execute(LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 2)))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("366 dias");
    }

    @Test
    void rejeitaPeriodoInvertidoOuIncompleto() {
        assertThatThrownBy(() -> useCase.execute(FIM, INICIO))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("anterior");
        assertThatThrownBy(() -> useCase.execute(null, FIM)).isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> useCase.execute(INICIO, null)).isInstanceOf(ValidationException.class);
        assertThat(repository.consultas).isEmpty();
    }

    private static class TemposDeExecucao implements OrdemServicoRepository {

        private List<Duration> tempos = List.of();
        private final List<List<Instant>> consultas = new ArrayList<>();

        @Override
        public List<Duration> temposDeExecucao(Instant concluidasDesde, Instant concluidasAte) {
            consultas.add(List.of(concluidasDesde, concluidasAte));
            return tempos;
        }

        @Override
        public OrdemServico save(OrdemServico ordemServico) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<OrdemServico> findById(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public PageResult<OrdemServico> findAll(List<StatusOrdemServico> status, Integer clienteId,
                                                Integer veiculoId, PageQuery pageQuery) {
            throw new UnsupportedOperationException();
        }
    }
}
