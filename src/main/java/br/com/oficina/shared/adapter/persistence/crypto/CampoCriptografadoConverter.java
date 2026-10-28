package br.com.oficina.shared.adapter.persistence.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Component
@Converter
public class CampoCriptografadoConverter implements AttributeConverter<String, String> {

    private final CriptografiaCampo criptografia;

    public CampoCriptografadoConverter(CriptografiaCampo criptografia) {
        this.criptografia = criptografia;
    }

    @Override
    public String convertToDatabaseColumn(String valor) {
        return criptografia.criptografar(valor);
    }

    @Override
    public String convertToEntityAttribute(String valor) {
        return criptografia.descriptografar(valor);
    }
}
