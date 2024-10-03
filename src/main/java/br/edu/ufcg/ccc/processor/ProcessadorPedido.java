package br.edu.ufcg.ccc.processor;

import java.util.Random;
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
    private final ECommece ecommerce;

    public ProcessadorPedido(
            BlockingQueue<Pedido> filaDePedidos,
            BlockingQueue<Pedido> pedidosProcessados,
            ConcurrentHashMap<Produto, Integer> stockQueue,
            BlockingQueue<Pedido> pedidosPendentes,
            ConcurrentSkipListSet<Integer> idPedidosPendentes,
            ECommece eCommece
    ) {
        this.filaDePedidos = filaDePedidos;
        this.pedidosProcessados = pedidosProcessados;
        this.stockQueue = stockQueue;
        this.pedidosPendentes = pedidosPendentes;
        this.idPedidosPendentes = idPedidosPendentes;
        this.ecommerce = eCommece;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // PRIMEIRO OLHA A FILA DE PENDENTES
                Pedido pedido = pedidosPendentes.poll();
                // SE NÃO TEM PENDENTES, ENTÃO USA OS  PEDIDOS ATUAIS E QUE NÃO ESTÃO PENDENTES.
                if (pedido == null) {
                    pedido = filaDePedidos.take();
                }
                
                if (processarPedido(pedido)) {
                    pedidosProcessados.add(pedido);
                    double valorTotal = pedido.getItensPedidos().stream()
                            .mapToDouble(item -> item.getQuantidade() * item.getProduto().getPreco())
                            .sum();
                    ecommerce.incrementarPedidosCompletos(valorTotal);
                    System.out.println(pedido);
                }else {
                    idPedidosPendentes.add(pedido.getId());
                    pedidosPendentes.add(pedido);
                }

                Thread.sleep(new Random().nextLong(0, 2000));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean processarPedido(Pedido pedido) {
        boolean pedidoCompleto = true;

        for (ItensPedido item : pedido.getItensPedidos()) {
            Produto produto = item.getProduto();
            int quantidadeDesejada = item.getQuantidade();
            Integer quantidadeEmEstoque = stockQueue.get(produto);
            if (quantidadeEmEstoque == null || quantidadeEmEstoque < quantidadeDesejada) {
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

        return pedidoCompleto;
    }
}
