package br.edu.ufcg.ccc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.edu.ufcg.ccc.client.Client;
import br.edu.ufcg.ccc.system.ECommece;

public class Main {
    public static void main(String[] args) {
        ECommece eCommece = new ECommece();
        ScheduledExecutorService client = Executors.newScheduledThreadPool(1);

        client.scheduleAtFixedRate(new Client(eCommece), 0, 8, TimeUnit.SECONDS);
    }
}