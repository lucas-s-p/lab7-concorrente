package br.edu.ufcg.ccc.processor;

import br.edu.ufcg.ccc.system.ECommece;

public class RelatorioVendas implements Runnable {
    private final ECommece ecommerce;

    public RelatorioVendas(ECommece ecommerce) {
        this.ecommerce = ecommerce;
    }

    @Override
    public void run() {
        int pedidosCompletos = ecommerce.getPedidosCompletos();
        double valorTotalVendas = ecommerce.getValorTotalVendas();
        int pedidosRejeitados = ecommerce.getPedidosRejeitados();

        System.out.println("\n=== Relatório de Vendas ===");
        System.out.println("Número total de pedidos processados: " + pedidosCompletos);
        System.out.println("Valor total das vendas: R$ " + valorTotalVendas);
        System.out.println("Número de pedidos rejeitados: " + pedidosRejeitados);
        System.out.println("===========================\n");
    }
}

