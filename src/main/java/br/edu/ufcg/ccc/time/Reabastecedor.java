package br.edu.ufcg.ccc.time;

import java.util.concurrent.ConcurrentHashMap;

import br.edu.ufcg.ccc.system.ECommece;
import br.edu.ufcg.ccc.system.Produto;

public class Reabastecedor implements Runnable{
    private final ConcurrentHashMap<Produto, Integer> estoque;
    private final ECommece ecommerce;
    
        public Reabastecedor(ConcurrentHashMap<Produto, Integer> estoque, ECommece ecommerce) {
            this.estoque = estoque;
            this.ecommerce = ecommerce;
        }

        @Override
        public void run() {
            synchronized (estoque) {
                for (Produto produto : estoque.keySet()) {
                    estoque.computeIfPresent(produto, (key, value) -> value + 10);
                    synchronized (System.out){
                        System.out.println("Reabastecendo " + produto.getNome() + ": Novo estoque = " + estoque.get(produto));
                    }
                }
                ecommerce.sinalizarReabastecimento();
            }
        }
}
