package br.com.oficina.infrastructure.dataproviders.database.crypto;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class CriptografiaCampoTest {

    private static final String CHAVE = Base64.getEncoder().encodeToString(new byte[32]);

    private final CriptografiaCampo criptografia = new CriptografiaCampo(CHAVE);
    private final IndiceCego indiceCego = new IndiceCego(CHAVE);

    @Test
    void criptografaEDescriptografa() {
        String cifrado = criptografia.criptografar("52998224725");

        assertThat(cifrado).startsWith("v1:").doesNotContain("52998224725");
        assertThat(criptografia.descriptografar(cifrado)).isEqualTo("52998224725");
    }

    @Test
    void mesmoValorGeraCifrasDiferentes() {
        assertThat(criptografia.criptografar("ABC1D23")).isNotEqualTo(criptografia.criptografar("ABC1D23"));
    }

    @Test
    void indiceCegoEDeterministico() {
        assertThat(indiceCego.hash("ABC1D23")).isEqualTo(indiceCego.hash("ABC1D23")).hasSize(64);
        assertThat(indiceCego.hash("ABC1D23")).isNotEqualTo(indiceCego.hash("ABC1D24"));
    }

    @Test
    void valorLegadoSemPrefixoEDevolvidoComoEsta() {
        assertThat(criptografia.descriptografar("52998224725")).isEqualTo("52998224725");
    }
}
