package br.edu.ufcg.ccc;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.ccc.system.ECommece;
import br.edu.ufcg.ccc.system.ItensPedido;
import br.edu.ufcg.ccc.system.Pedido;
import br.edu.ufcg.ccc.system.Produto;

public class Main {
    public static void main(String[] args) {
        Produto produto = new Produto("produto1", 13L);
        List<ItensPedido> itensPedidoList = new ArrayList<>();
        itensPedidoList.add(new ItensPedido(10, produto));
        List<Pedido> pedidos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            pedidos.add(new Pedido(itensPedidoList));
        }

        ECommece eCommece = new ECommece();
        for (Pedido p : pedidos){
            eCommece.criarPedido(p);
        }
    }
}