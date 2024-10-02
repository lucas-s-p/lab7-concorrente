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
        Produto produto = new Produto("Feijão", 13L);
        Produto produto_1 = new Produto("Arroz", 5L);
        
        // SISTEMA
        ECommece eCommece = new ECommece();

        //ESTOQUE
        eCommece.adicionarProdutoEstoque(produto);
        eCommece.adicionarProdutoEstoque(produto_1);

        // CLIENTES
        Client clientOne = new  Client(eCommece);
        //Client clienttwo = new  Client(eCommece);

        //SIMULA COMPRA DO CLIENTE ONE
        clientOne.adicionaProduto(produto, 10);
        clientOne.adicionaProduto(produto_1, 4);
        ScheduledExecutorService client = Executors.newScheduledThreadPool(1);
        client.scheduleAtFixedRate(clientOne, 0, 50, TimeUnit.SECONDS);

        //SIMULA COMPRA DO CLIENTE TWO
        //clienttwo.adicionaProduto(produto, 10);
        //clienttwo.adicionaProduto(produto_1, 4);
        //client.scheduleAtFixedRate(clienttwo, 0, 8, TimeUnit.SECONDS);
    }
}