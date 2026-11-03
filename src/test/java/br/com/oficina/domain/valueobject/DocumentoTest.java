package br.com.oficina.domain.valueobject;

import br.com.oficina.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DocumentoTest {

    @Test
    void aceitaCpfFormatadoENormaliza() {
        Documento cpf = new Documento("529.982.247-25");

        assertThat(cpf.valor()).isEqualTo("52998224725");
        assertThat(cpf.tipo()).isEqualTo(Documento.Tipo.CPF);
    }

    @Test
    void aceitaCnpjFormatadoENormaliza() {
        Documento cnpj = new Documento("11.222.333/0001-81");

        assertThat(cnpj.valor()).isEqualTo("11222333000181");
        assertThat(cnpj.tipo()).isEqualTo(Documento.Tipo.CNPJ);
    }

    @Test
    void toStringNaoExpoeODocumento() {
        assertThat(new Documento("52998224725")).hasToString("Documento[CPF ***25]");
        assertThat(new Documento("11222333000181")).hasToString("Documento[CNPJ ***81]");
    }

    @ParameterizedTest
    @ValueSource(strings = {"52998224700", "52998224715", "11111111111", "11222333000182", "11222333000191",
            "22222222222222", "123", "", "529982247251"})
    void recusaDocumentoInvalido(String valor) {
        assertThatThrownBy(() -> new Documento(valor))
                .isInstanceOf(ValidationException.class)
                .hasMessage("CPF/CNPJ inválido");
    }

    @Test
    void recusaDocumentoAusente() {
        assertThatThrownBy(() -> new Documento(null))
                .isInstanceOf(ValidationException.class)
                .hasMessage("CPF/CNPJ é obrigatório");
    }
}
