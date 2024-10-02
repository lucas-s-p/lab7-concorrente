package br.edu.ufcg.ccc.time;

import java.util.concurrent.ConcurrentHashMap;

import br.edu.ufcg.ccc.system.Produto;

public class Reabastecedor implements Runnable{
    private final ConcurrentHashMap<Produto, Integer> estoque;

        public Reabastecedor(ConcurrentHashMap<Produto, Integer> estoque) {
            this.estoque = estoque;
        }

        @Override
        public void run() {
            for (Produto produto : estoque.keySet()) {
                estoque.computeIfPresent(produto, (key, value) -> value + 10);
                System.out.println("Reabastecendo " + produto.getNome() + ": Novo estoque = " + estoque.get(produto));
            }
        }
}
