package br.com.oficina.shared.adapter.persistence.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class CriptografiaCampo {

    private static final String PREFIXO = "v1:";
    private static final String ALGORITMO = "AES/GCM/NoPadding";
    private static final int TAMANHO_IV = 12;
    private static final int TAMANHO_TAG_BITS = 128;

    private final SecretKeySpec chave;
    private final SecureRandom random = new SecureRandom();

    public CriptografiaCampo(@Value("${app.security.crypto.chave}") String chaveBase64) {
        byte[] bytes = Base64.getDecoder().decode(chaveBase64);
        if (bytes.length != 32) {
            throw new IllegalStateException("app.security.crypto.chave deve ter 32 bytes em Base64 (AES-256)");
        }
        this.chave = new SecretKeySpec(bytes, "AES");
    }

    public String criptografar(String texto) {
        if (texto == null) {
            return null;
        }
        try {
            byte[] iv = new byte[TAMANHO_IV];
            random.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, chave, new GCMParameterSpec(TAMANHO_TAG_BITS, iv));
            byte[] cifrado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
            byte[] saida = ByteBuffer.allocate(iv.length + cifrado.length).put(iv).put(cifrado).array();
            return PREFIXO + Base64.getEncoder().encodeToString(saida);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Falha ao criptografar campo", e);
        }
    }

    public String descriptografar(String valor) {
        if (valor == null || !valor.startsWith(PREFIXO)) {
            return valor;
        }
        try {
            byte[] entrada = Base64.getDecoder().decode(valor.substring(PREFIXO.length()));
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, chave, new GCMParameterSpec(TAMANHO_TAG_BITS, entrada, 0, TAMANHO_IV));
            byte[] texto = cipher.doFinal(entrada, TAMANHO_IV, entrada.length - TAMANHO_IV);
            return new String(texto, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Falha ao descriptografar campo", e);
        }
    }
}
