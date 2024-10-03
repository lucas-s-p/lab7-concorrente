package br.edu.ufcg.ccc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.edu.ufcg.ccc.client.Client;
import br.edu.ufcg.ccc.system.ECommece;
import br.edu.ufcg.ccc.system.Produto;

public class Main {
    public static void main(String[] args) {
        // PRODUTOS
        Produto produto = new Produto("Feijão", 13);
        Produto produto_1 = new Produto("Arroz", 5);
        Produto produto_2 = new Produto("Macarrão", 10);
        
        // SISTEMA
        ECommece eCommece = new ECommece();

        // ESTOQUE
        eCommece.adicionarProdutoEstoque(produto, 5);
        eCommece.adicionarProdutoEstoque(produto_1, 8);
        eCommece.adicionarProdutoEstoque(produto_2, 3);

        // CLIENTES
        Client clientOne = new Client(eCommece, "A");
        Client clienttwo = new Client(eCommece, "B");
        Client clientThree = new Client(eCommece, "C");

        // SIMULA COMPRA DO CLIENTE ONE
        clientOne.adicionaProduto(produto, 10);
        clientOne.adicionaProduto(produto_1, 4);
        ScheduledExecutorService client = Executors.newScheduledThreadPool(8);
        client.scheduleAtFixedRate(clientOne, 0, 5, TimeUnit.SECONDS);

        // SIMULA COMPRA DO CLIENTE TWO
        clienttwo.adicionaProduto(produto, 10);
        clienttwo.adicionaProduto(produto_1, 4);
        client.scheduleAtFixedRate(clienttwo, 0, 5, TimeUnit.SECONDS);

        // SIMULA COMPRA DO CLIENTE THREE
        clientThree.adicionaProduto(produto, 4);
        clientThree.adicionaProduto(produto_2, 15);
        client.scheduleAtFixedRate(clientThree, 0, 5, TimeUnit.SECONDS);
    }
}