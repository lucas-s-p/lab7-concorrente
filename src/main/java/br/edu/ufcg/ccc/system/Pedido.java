package br.edu.ufcg.ccc.system;

import java.util.List;
import java.util.Objects;

public class Pedido {
    List<ItensPedido> itensPedidos;

    @Override
    public String toString() {
        return "Pedido{" +
                "itensPedidos=" + itensPedidos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(itensPedidos, pedido.itensPedidos);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(itensPedidos);
    }

    public List<ItensPedido> getItensPedidos() {
        return itensPedidos;
    }

    public void setItensPedidos(List<ItensPedido> itensPedidos) {
        this.itensPedidos = itensPedidos;
    }

    public Pedido(List<ItensPedido> itensPedidos) {
        this.itensPedidos = itensPedidos;
    }
}
