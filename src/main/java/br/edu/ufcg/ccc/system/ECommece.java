package br.edu.ufcg.ccc.system;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.edu.ufcg.ccc.processor.ProcessadorPedido;
import br.edu.ufcg.ccc.processor.RelatorioVendas;
import br.edu.ufcg.ccc.time.Reabastecedor;

public class ECommece {
    private final BlockingQueue<Pedido> requestQueue;
    private final ConcurrentHashMap<Produto, Integer> stockQueue;
    private final ExecutorService processadoresDePedidos;
    private final BlockingQueue<Pedido> pedidosProcessados;
    private final ScheduledExecutorService reabastecedorExecutor;
    private final ScheduledExecutorService relatorioExecutor;

    private int pedidosCompletos = 0;
    private int pedidosRejeitados = 0;
    private double valorTotalVendas = 0.0;

    public ECommece() {
        this.requestQueue = new ArrayBlockingQueue<>(8);
        this.stockQueue = new ConcurrentHashMap<>();
        this.pedidosProcessados = new LinkedBlockingQueue<>();
        this.processadoresDePedidos = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            this.processadoresDePedidos.submit(new ProcessadorPedido(requestQueue, pedidosProcessados, stockQueue, this));
        }


        this.reabastecedorExecutor = Executors.newScheduledThreadPool(1);
        this.reabastecedorExecutor.scheduleAtFixedRate(new Reabastecedor(this.stockQueue), 0, 10, TimeUnit.SECONDS);

        // Inicializa o gerador de relatórios
        this.relatorioExecutor = Executors.newScheduledThreadPool(1);
        this.relatorioExecutor.scheduleAtFixedRate(new RelatorioVendas(this), 0, 30, TimeUnit.SECONDS);
    }

    public void criarPedido(Pedido pedido){
        while (!requestQueue.offer(pedido)){
            System.out.println("Pedido barrado na fila");
        }
        // System.out.println("Pedido Criado");  // TIRE O COMENTÁRIO PARA TESTAR
    }

    public void adicionarProdutoEstoque(Produto produto, int quantidadeProd) {
        this.stockQueue.put(produto, quantidadeProd);
    }

    // Métodos para atualizar contadores
    public synchronized void incrementarPedidosCompletos(double valor) {
        pedidosCompletos++;
        valorTotalVendas += valor;
    }

    public synchronized void incrementarPedidosRejeitados() {
        pedidosRejeitados++;
    }

    public synchronized int getPedidosCompletos() {
        return pedidosCompletos;
    }

    public synchronized double getValorTotalVendas() {
        return valorTotalVendas;
    }

    public synchronized int getPedidosRejeitados() {
        return pedidosRejeitados;
    }
}
