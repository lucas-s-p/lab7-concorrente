package br.edu.ufcg.ccc.client;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.ccc.system.ECommece;
import br.edu.ufcg.ccc.system.ItensPedido;
import br.edu.ufcg.ccc.system.Pedido;
import br.edu.ufcg.ccc.system.Produto;

public class Client implements Runnable{

    private final ECommece eCommece;
    private final List<ItensPedido> itensPedidos;
    private final String nomeCliente;

    public Client(ECommece eCommece, String nomeCliente) {
        this.eCommece = eCommece;
        this.itensPedidos = new ArrayList<>();
        this.nomeCliente = nomeCliente;
    }

    @Override
    public void run() {
        Pedido pedido = new Pedido(this.itensPedidos, nomeCliente);
        eCommece.criarPedido(pedido);
    }

    public void adicionaProduto(Produto produto, int quantidadeDesejada) {
        this.itensPedidos.add(new ItensPedido(quantidadeDesejada, produto));
    }
    
}
