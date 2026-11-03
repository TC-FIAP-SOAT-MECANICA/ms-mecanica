package br.com.oficina.domain.model.cadastro;

import br.com.oficina.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VeiculoTest {

    private static final int ANO_ATUAL = 2026;

    private static DadosVeiculo dados(String marca, Integer ano, String cor, String chassi, Integer quilometragem) {
        return new DadosVeiculo(1, new Placa("ABC1D23"), marca, " Gol 1.6 ", ano, cor, chassi, quilometragem);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABC1234", "abc-1234", "ABC1D23", "abc 1d23"})
    void placaAceitaPadraoAntigoEMercosul(String valor) {
        assertThat(new Placa(valor).valor()).matches("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$");
    }

    @Test
    void placaNormalizaERecusaFormatoInvalido() {
        assertThat(new Placa("abc-1234")).hasToString("ABC1234");
        assertThatThrownBy(() -> new Placa("AB12345")).isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> new Placa(null))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Placa é obrigatória");
    }

    @Test
    void cadastroNormalizaOsDados() {
        Veiculo veiculo = Veiculo.cadastrar(dados(" Volkswagen ", 2019, "  ", " 9bwzzz377vt004251 ", null), ANO_ATUAL);

        assertThat(veiculo.marca()).isEqualTo("Volkswagen");
        assertThat(veiculo.modelo()).isEqualTo("Gol 1.6");
        assertThat(veiculo.cor()).isNull();
        assertThat(veiculo.chassi()).isEqualTo("9BWZZZ377VT004251");
        assertThat(veiculo.quilometragem()).isNull();
        assertThat(Veiculo.normalizarChassi(null)).isNull();
    }

    @Test
    void aceitaAnoDoProximoModelo() {
        assertThat(Veiculo.cadastrar(dados("Fiat", ANO_ATUAL + 1, "Preto", null, 0), ANO_ATUAL).ano())
                .isEqualTo(ANO_ATUAL + 1);
    }

    @Test
    void recusaAnoForaDoIntervalo() {
        assertThatThrownBy(() -> Veiculo.cadastrar(dados("Fiat", 1949, null, null, null), ANO_ATUAL))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> Veiculo.cadastrar(dados("Fiat", ANO_ATUAL + 2, null, null, null), ANO_ATUAL))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> Veiculo.cadastrar(dados("Fiat", null, null, null, null), ANO_ATUAL))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void recusaMarcaVaziaEQuilometragemNegativa() {
        assertThatThrownBy(() -> Veiculo.cadastrar(dados(" ", 2019, null, null, null), ANO_ATUAL))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Marca é obrigatório");
        assertThatThrownBy(() -> Veiculo.cadastrar(dados(null, 2019, null, null, null), ANO_ATUAL))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> Veiculo.cadastrar(dados("Fiat", 2019, null, null, -1), ANO_ATUAL))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Quilometragem não pode ser negativa");
    }

    @Test
    void atualizaMantendoOId() {
        Veiculo veiculo = Veiculo.restaurar(5, dados("Fiat", 2019, null, null, 100));

        veiculo.atualizar(dados("Fiat", 2019, "Azul", null, 200), ANO_ATUAL);

        assertThat(veiculo.id()).isEqualTo(5);
        assertThat(veiculo.cor()).isEqualTo("Azul");
        assertThat(veiculo.quilometragem()).isEqualTo(200);
    }
}
