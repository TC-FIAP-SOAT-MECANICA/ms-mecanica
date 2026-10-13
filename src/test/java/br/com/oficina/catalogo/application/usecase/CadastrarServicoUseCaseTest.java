package br.com.oficina.catalogo.application.usecase;

import br.com.oficina.catalogo.application.dto.CadastrarServicoInput;
import br.com.oficina.catalogo.domain.Servico;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testa o caso de uso sem banco e sem Spring: o port é substituído por uma lista em memória.
 * Essa é a vantagem prática da arquitetura — a regra de negócio é testável isoladamente.
 */
class CadastrarServicoUseCaseTest {

    private final List<Servico> salvos = new ArrayList<>();
    private final CadastrarServicoUseCase useCase = new CadastrarServicoUseCase(salvos::add);

    @Test
    void cadastraServicoAtivoESalva() {
        var output = useCase.execute(new CadastrarServicoInput("Troca de óleo", null, "149.9", 45));

        assertThat(output.id()).isNotNull();
        assertThat(output.preco()).isEqualTo("149.90");
        assertThat(output.ativo()).isTrue();
        assertThat(salvos).hasSize(1);
    }

    @Test
    void recusaTempoEstimadoInvalidoSemSalvar() {
        assertThatThrownBy(() -> useCase.execute(new CadastrarServicoInput("Alinhamento", null, "80.00", 0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Tempo estimado");
        assertThat(salvos).isEmpty();
    }
}
