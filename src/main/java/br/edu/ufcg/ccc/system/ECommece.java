package br.edu.ufcg.ccc.system;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
    private final BlockingQueue<Pedido> pedidosPendentes;
    private final ConcurrentSkipListSet<Integer> idPedidosPendentes;
    private int pedidosCompletos = 0;
    private double valorTotalVendas = 0.0;

    private final AtomicInteger idPedido;

    private final ReentrantLock reabastecimentoLock = new ReentrantLock();
    private final Condition reabastecido = reabastecimentoLock.newCondition();

    public ECommece() {
        this.requestQueue = new ArrayBlockingQueue<>(8);
        this.stockQueue = new ConcurrentHashMap<>();
        this.pedidosProcessados = new LinkedBlockingQueue<>();
        this.processadoresDePedidos = Executors.newFixedThreadPool(5);
        this.idPedido = new AtomicInteger(0);
        this.pedidosPendentes = new LinkedBlockingQueue<>();
        this.idPedidosPendentes = new ConcurrentSkipListSet<>();

        for (int i = 0; i < 5; i++) {
            this.processadoresDePedidos.submit(new ProcessadorPedido(requestQueue, pedidosProcessados, stockQueue, pedidosPendentes, idPedidosPendentes, this));
        }


        this.reabastecedorExecutor = Executors.newScheduledThreadPool(1);
        this.reabastecedorExecutor.scheduleAtFixedRate(new Reabastecedor(this.stockQueue, this), 0, 10, TimeUnit.SECONDS);

        this.relatorioExecutor = Executors.newScheduledThreadPool(1);
        this.relatorioExecutor.scheduleAtFixedRate(new RelatorioVendas(this), 0, 30, TimeUnit.SECONDS);
    }

    public void sinalizarReabastecimento() {
        reabastecimentoLock.lock();
        try {
            reabastecido.signalAll();
        } finally {
            reabastecimentoLock.unlock();
        }
    }

    public void aguardarReabastecimento() throws InterruptedException {
        reabastecimentoLock.lock();
        try {
            reabastecido.await();
        } finally {
            reabastecimentoLock.unlock();
        }
    }

    public void criarPedido(Pedido pedido){
        pedido.setId(idPedido.incrementAndGet());
        while (!requestQueue.offer(pedido)){
            System.out.println("Pedido barrado na fila");
        }
        // System.out.println("Pedido Criado");  // TIRE O COMENTÁRIO PARA TESTAR
    }

    public void adicionarProdutoEstoque(Produto produto, int quantidadeProd) {
        this.stockQueue.put(produto, quantidadeProd);
    }

    public synchronized void incrementarPedidosCompletos(double valor) {
        pedidosCompletos++;
        valorTotalVendas += valor;
    }

    public synchronized int getPedidosCompletos() {
        return pedidosCompletos;
    }

    public synchronized double getValorTotalVendas() {
        return valorTotalVendas;
    }

    public synchronized int getPedidosRejeitados() {
        return idPedidosPendentes.size();
    }

}
