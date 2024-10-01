package br.edu.ufcg.ccc;

import br.edu.ufcg.ccc.system.ECommece;
import br.edu.ufcg.ccc.system.ItensPedido;
import br.edu.ufcg.ccc.system.Pedido;
import br.edu.ufcg.ccc.system.Produto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        Produto produto = new Produto("produto1", 13L);
        List<ItensPedido> itensPedidoList = new ArrayList<>();
        itensPedidoList.add(new ItensPedido(5, produto));
        List<Pedido> pedidos = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            pedidos.add(new Pedido(itensPedidoList));
        }

        ECommece eCommece = new ECommece();
        for (Pedido p : pedidos){
            eCommece.criarPedido(p);
        }
    }
}