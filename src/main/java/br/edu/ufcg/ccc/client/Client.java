package br.edu.ufcg.ccc.client;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.ccc.system.ECommece;
import br.edu.ufcg.ccc.system.ItensPedido;
import br.edu.ufcg.ccc.system.Pedido;
import br.edu.ufcg.ccc.system.Produto;

public class Client implements Runnable{

    private final ECommece eCommece;

    public Client(ECommece eCommece) {
        this.eCommece = eCommece;
    }

    @Override
    public void run() {
        Produto produto = new Produto("produto1", 13L);
        List<ItensPedido> itensPedidoList = new ArrayList<>();
        itensPedidoList.add(new ItensPedido(10, produto));

        Pedido pedido = new Pedido(itensPedidoList);

        eCommece.criarPedido(pedido);
    }
    
}
