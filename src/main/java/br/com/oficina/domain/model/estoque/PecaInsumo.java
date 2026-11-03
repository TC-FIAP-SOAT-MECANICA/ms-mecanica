package br.com.oficina.domain.model.estoque;

import br.com.oficina.domain.exceptions.BusinessRuleException;
import br.com.oficina.domain.exceptions.ValidationException;
import br.com.oficina.domain.valueobject.Dinheiro;
import br.com.oficina.domain.valueobject.Quantidade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class PecaInsumo {

    private final Integer id;
    private String sku;
    private String nome;
    private UnidadeMedida unidadeMedida;
    private Dinheiro precoCusto;
    private Dinheiro precoVenda;
    private BigDecimal quantidadeEstoque;
    private BigDecimal estoqueMinimo;

    private PecaInsumo(Integer id, BigDecimal quantidadeEstoque) {
        this.id = id;
        this.quantidadeEstoque = escala(quantidadeEstoque);
    }

    public static PecaInsumo cadastrar(DadosPecaInsumo dados) {
        var peca = new PecaInsumo(null, BigDecimal.ZERO);
        peca.atualizarCadastro(dados);
        return peca;
    }

    public static PecaInsumo restaurar(Integer id, DadosPecaInsumo dados, BigDecimal quantidadeEstoque) {
        var peca = new PecaInsumo(id, quantidadeEstoque);
        peca.aplicar(dados);
        return peca;
    }

    public void atualizarCadastro(DadosPecaInsumo dados) {
        if (dados.sku() == null || dados.sku().isBlank()) {
            throw new ValidationException("SKU é obrigatório");
        }
        if (dados.nome() == null || dados.nome().isBlank()) {
            throw new ValidationException("Nome da peça é obrigatório");
        }
        UnidadeMedida unidade = new UnidadeMedida(dados.unidadeMedida());
        if (dados.estoqueMinimo() == null || dados.estoqueMinimo().signum() < 0) {
            throw new ValidationException("Estoque mínimo deve ser zero ou positivo");
        }
        unidade.validar(dados.estoqueMinimo());
        unidade.validar(quantidadeEstoque);
        aplicar(new DadosPecaInsumo(dados.sku().trim(), dados.nome().trim(), unidade.codigo(),
                Objects.requireNonNull(dados.precoCusto(), "precoCusto"),
                Objects.requireNonNull(dados.precoVenda(), "precoVenda"), dados.estoqueMinimo()));
    }

    public void validarQuantidade(Quantidade quantidade) {
        unidadeMedida.validar(quantidade.valor());
    }

    public void darEntrada(Quantidade quantidade) {
        validarQuantidade(quantidade);
        quantidadeEstoque = quantidadeEstoque.add(quantidade.valor());
    }

    public void darSaida(Quantidade quantidade) {
        validarQuantidade(quantidade);
        if (quantidadeEstoque.compareTo(quantidade.valor()) < 0) {
            throw new BusinessRuleException("ESTOQUE_INSUFICIENTE", "Estoque insuficiente para " + nome
                    + ": disponível " + quantidadeEstoque.toPlainString() + ", solicitado " + quantidade);
        }
        quantidadeEstoque = quantidadeEstoque.subtract(quantidade.valor());
    }

    public void ajustarInventario(BigDecimal saldoContado) {
        if (saldoContado == null || saldoContado.signum() < 0) {
            throw new ValidationException("Saldo contado deve ser zero ou positivo");
        }
        unidadeMedida.validar(saldoContado);
        quantidadeEstoque = escala(saldoContado);
    }

    public boolean abaixoDoMinimo() {
        return quantidadeEstoque.compareTo(estoqueMinimo) < 0;
    }

    private void aplicar(DadosPecaInsumo dados) {
        this.sku = dados.sku();
        this.nome = dados.nome();
        this.unidadeMedida = new UnidadeMedida(dados.unidadeMedida());
        this.precoCusto = dados.precoCusto();
        this.precoVenda = dados.precoVenda();
        this.estoqueMinimo = escala(dados.estoqueMinimo());
    }

    private static BigDecimal escala(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Integer id() {
        return id;
    }

    public String sku() {
        return sku;
    }

    public String nome() {
        return nome;
    }

    public String unidadeMedida() {
        return unidadeMedida.codigo();
    }

    public Dinheiro precoCusto() {
        return precoCusto;
    }

    public Dinheiro precoVenda() {
        return precoVenda;
    }

    public BigDecimal quantidadeEstoque() {
        return quantidadeEstoque;
    }

    public BigDecimal estoqueMinimo() {
        return estoqueMinimo;
    }
}
