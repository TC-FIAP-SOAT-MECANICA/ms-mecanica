package br.com.oficina.application.dto.shared;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageQueryTest {

    @Test
    void usaPadroesQuandoNaoInformado() {
        assertThat(PageQuery.of(null, null)).isEqualTo(new PageQuery(0, 20));
    }

    @Test
    void corrigeValoresForaDosLimites() {
        assertThat(PageQuery.of(-3, 0)).isEqualTo(new PageQuery(0, 20));
        assertThat(PageQuery.of(2, 500)).isEqualTo(new PageQuery(2, 100));
        assertThat(PageQuery.of(1, 15)).isEqualTo(new PageQuery(1, 15));
    }

    @Test
    void pageResultConverteOConteudoMantendoAPaginacao() {
        PageResult<Integer> pagina = new PageResult<>(List.of(1, 2), 0, 2, 5, 3);

        PageResult<String> convertida = pagina.map(numero -> "n" + numero);

        assertThat(convertida.content()).containsExactly("n1", "n2");
        assertThat(convertida.totalElements()).isEqualTo(5);
        assertThat(convertida.totalPages()).isEqualTo(3);
    }
}
