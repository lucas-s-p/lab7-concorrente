package br.edu.ufcg.ccc.processor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import br.edu.ufcg.ccc.system.ECommece;
import br.edu.ufcg.ccc.system.ItensPedido;
import br.edu.ufcg.ccc.system.Pedido;
import br.edu.ufcg.ccc.system.Produto;

public class ProcessadorPedido implements Runnable {
    private final BlockingQueue<Pedido> filaDePedidos;
    private final BlockingQueue<Pedido> pedidosProcessados;
    private final ConcurrentHashMap<Produto, Integer> stockQueue;
    private final BlockingQueue<Pedido> pedidosPendentes;
    private final ConcurrentSkipListSet<Integer> idPedidosPendentes;
    private final Object processLock;
    private final ECommece ecommerce;

    public ProcessadorPedido(
            BlockingQueue<Pedido> filaDePedidos,
            BlockingQueue<Pedido> pedidosProcessados,
            ConcurrentHashMap<Produto, Integer> stockQueue,
            BlockingQueue<Pedido> pedidosPendentes,
            ConcurrentSkipListSet<Integer> idPedidosPendentes, Object processLock,
            ECommece eCommece
    ) {
        this.filaDePedidos = filaDePedidos;
        this.pedidosProcessados = pedidosProcessados;
        this.stockQueue = stockQueue;
        this.pedidosPendentes = pedidosPendentes;
        this.idPedidosPendentes = idPedidosPendentes;
        this.processLock = processLock;
        this.ecommerce = eCommece;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Pedido pedido = pedidosPendentes.poll();
    
                if (pedido == null) {
                    pedido = filaDePedidos.take();
                }
    
                if (processarPedido(pedido)) {
                    pedidosProcessados.add(pedido);
                    double valorTotal = pedido.getItensPedidos().stream()
                            .mapToDouble(item -> item.getQuantidade() * item.getProduto().getPreco())
                            .sum();
                    ecommerce.incrementarPedidosCompletos(valorTotal);
                    synchronized (System.out){
                        System.out.println(pedido);
                    }
                } else {
                    idPedidosPendentes.add(pedido.getId());
                    pedidosPendentes.add(pedido);
    
                    ecommerce.aguardarReabastecimento();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean processarPedido(Pedido pedido) throws InterruptedException {
        boolean pedidoCompleto = true;
        synchronized (processLock) {
            for (ItensPedido item : pedido.getItensPedidos()) {
                Produto produto = item.getProduto();
                int quantidadeDesejada = item.getQuantidade();
                if (stockQueue.get(produto) == null || stockQueue.get(produto) < quantidadeDesejada) {
                    pedidoCompleto = false;
                    break;
                }
            }

            if (pedidoCompleto) {
                for (ItensPedido item : pedido.getItensPedidos()) {
                    Produto produto = item.getProduto();
                    int quantidadeDesejada = item.getQuantidade();
                    stockQueue.computeIfPresent(produto, (key, value) -> value - quantidadeDesejada);
                }
            }
        }
        return pedidoCompleto;
    }
}