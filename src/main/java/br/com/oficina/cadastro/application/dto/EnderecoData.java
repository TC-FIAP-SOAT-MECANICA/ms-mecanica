package br.com.oficina.cadastro.application.dto;

import br.com.oficina.cadastro.domain.Endereco;

/** Endereço como dado simples, usado na entrada e na saída dos casos de uso. */
public record EnderecoData(String logradouro, String numero, String complemento, String bairro, String cidade,
                           String uf, String cep) {

    public Endereco toDomain() {
        return new Endereco(logradouro, numero, complemento, bairro, cidade, uf, cep);
    }

    public static EnderecoData from(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoData(endereco.logradouro(), endereco.numero(), endereco.complemento(),
                endereco.bairro(), endereco.cidade(), endereco.uf(), endereco.cep());
    }
}
