package br.edu.ufcg.ccc.system;

import java.util.List;
import java.util.Objects;

public class Pedido {
    List<ItensPedido> itensPedidos;
    String nomeCliente;
    int pedidoId;

    @Override
    public String toString() {
        return "Pedido " + pedidoId + " do Cliente " + nomeCliente + " foi processado";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(pedidoId, pedido.pedidoId);
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

    public void setId(int pedidoId){
        this.pedidoId = pedidoId;
    }

    public int getId(){
        return pedidoId;
    }

    public Pedido(List<ItensPedido> itensPedidos, String nomeCliente) {
        this.itensPedidos = itensPedidos;
        this.nomeCliente = nomeCliente;
    }

}
