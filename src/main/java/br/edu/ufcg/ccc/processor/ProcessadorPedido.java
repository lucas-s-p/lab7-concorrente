package br.edu.ufcg.ccc.processor;

import br.edu.ufcg.ccc.system.ItensPedido;
import br.edu.ufcg.ccc.system.Pedido;
import br.edu.ufcg.ccc.system.Produto;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessadorPedido implements Runnable{
    private BlockingQueue<Pedido> filaDePedidos;
    private BlockingQueue<Pedido> pedidosProcessados;
    private ConcurrentHashMap<Produto, Integer> stockQueue;
    private final Object lock;


    public ProcessadorPedido(
            BlockingQueue<Pedido> filaDePedidos,
            BlockingQueue<Pedido> pedidosProcessados,
            ConcurrentHashMap<Produto, Integer> stockQueue
    ) {
        this.filaDePedidos = filaDePedidos;
        this.pedidosProcessados = pedidosProcessados;
        this.stockQueue = stockQueue;
        this.lock = new Object();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Pedido pedido = filaDePedidos.take();
                List<ItensPedido> itensPedido = pedido.getItensPedidos();
                for (ItensPedido i : itensPedido) {
                    synchronized (lock) {
                        if (stockQueue.containsKey(i.getProduto())) {
                            if (stockQueue.get(i.getProduto()) >= i.getQuantidade()) {
                                stockQueue.put(i.getProduto(), stockQueue.get(i.getProduto()) - i.getQuantidade());
                                this.pedidosProcessados.add(pedido);
                                System.out.println("Pedido completo");
                            } else {
                                System.out.println("Quantidade insuficiente");
                            }
                        } else {
                            System.out.println("Produto não existe");
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
