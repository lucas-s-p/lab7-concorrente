package br.edu.ufcg.ccc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.edu.ufcg.ccc.client.Client;
import br.edu.ufcg.ccc.system.ECommece;
import br.edu.ufcg.ccc.system.Produto;

public class Main {
    public static void main(String[] args) {
        //PRODUTOS
        Produto produto = new Produto("Feijão", 13);
        Produto produto_1 = new Produto("Arroz", 5);
        
        // SISTEMA
        ECommece eCommece = new ECommece();

        //ESTOQUE
        eCommece.adicionarProdutoEstoque(produto, 5);
        eCommece.adicionarProdutoEstoque(produto_1, 8);

        // CLIENTES
        Client clientOne = new  Client(eCommece, "A");
        Client clienttwo = new  Client(eCommece, "B");

        //SIMULA COMPRA DO CLIENTE ONE
        clientOne.adicionaProduto(produto, 10);
        clientOne.adicionaProduto(produto_1, 4);
        ScheduledExecutorService client = Executors.newScheduledThreadPool(1);
        client.scheduleAtFixedRate(clientOne, 0, 5000, TimeUnit.SECONDS);

        //SIMULA COMPRA DO CLIENTE TWO
        clienttwo.adicionaProduto(produto, 10);
        clienttwo.adicionaProduto(produto_1, 4);
        client.scheduleAtFixedRate(clienttwo, 0, 15, TimeUnit.SECONDS);
    }
}