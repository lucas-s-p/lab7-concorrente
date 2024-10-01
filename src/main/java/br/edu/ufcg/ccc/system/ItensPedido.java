package br.edu.ufcg.ccc.system;

import java.util.Objects;

public class ItensPedido {
    private int quantidade;
    private Produto produto;

    public ItensPedido(int quantidade, Produto produto) {
        this.quantidade = quantidade;
        this.produto = produto;
    }

    @Override
    public String toString() {
        return "ItensPedido{" +
                "quantidade=" + quantidade +
                ", produto=" + produto +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItensPedido that = (ItensPedido) o;
        return quantidade == that.quantidade && Objects.equals(produto, that.produto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantidade, produto);
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
