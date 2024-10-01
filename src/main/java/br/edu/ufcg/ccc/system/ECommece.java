package br.edu.ufcg.ccc.system;


import br.edu.ufcg.ccc.processor.ProcessadorPedido;

import java.util.concurrent.*;

public class ECommece {
    private BlockingQueue<Pedido> requestQueue;
    private ConcurrentHashMap<Produto, Integer> stockQueue;
    private ScheduledExecutorService processadoresDePedidos;
    private BlockingQueue<Pedido> pedidosProcessados;

    public ECommece() {
        this.requestQueue = new ArrayBlockingQueue<>(8);
        this.stockQueue = new ConcurrentHashMap<>();
        this.stockQueue.put(new Produto("produto1", 13L), 10);
        this.pedidosProcessados = new LinkedBlockingQueue<>();
        this.processadoresDePedidos = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 5; i++) {
            this.processadoresDePedidos.submit(new ProcessadorPedido(requestQueue, pedidosProcessados, stockQueue));
        }
    }

    public void criarPedido(Pedido pedido){
        while (!requestQueue.offer(pedido)){
            System.out.println("Pedido barrado na fila");
        }
        System.out.println("Pedido Criado");
    }
}
