package br.com.oficina.shared.adapter.persistence.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HexFormat;

@Component
public class IndiceCego {

    private static final String ALGORITMO = "HmacSHA256";

    private final SecretKeySpec chave;

    public IndiceCego(@Value("${app.security.crypto.chave-indice}") String chaveBase64) {
        byte[] bytes = Base64.getDecoder().decode(chaveBase64);
        if (bytes.length < 32) {
            throw new IllegalStateException("app.security.crypto.chave-indice deve ter ao menos 32 bytes em Base64");
        }
        this.chave = new SecretKeySpec(bytes, ALGORITMO);
    }

    public String hash(String valor) {
        try {
            Mac mac = Mac.getInstance(ALGORITMO);
            mac.init(chave);
            return HexFormat.of().formatHex(mac.doFinal(valor.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Falha ao calcular índice cego", e);
        }
    }
}
