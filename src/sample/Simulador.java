package sample;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static sample.Main.TIMEOUT;
import static sample.Main.WAITING;

public class Simulador extends Thread {

    @Override
    public void run() {
        try {
            simular();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public void simular() throws GeneralSecurityException {
        MulticastSocket s = null;
        try {
            Server servidor = new Server();
            InetAddress group = servidor.criarGrupo();
            Recurso processador = new Recurso("Recurso 001", WAITING);
            Recurso memoria = new Recurso("Recurso 002", WAITING);
            List<Recurso> recursos = Arrays.asList(processador, memoria);
            Manager manager = new Manager(recursos);
            servidor.configurar(group, "Server 01", manager);

            final CyclicBarrier initial_gate = new CyclicBarrier(4);
            Client client = new Client(group, "Cliente A", initial_gate, manager);
            Client client1 = new Client(group, "Cliente B", initial_gate, manager);
            Client client2 = new Client(group, "Cliente C", initial_gate, manager);
            Client client3 = new Client(group, "Cliente D", initial_gate, manager);

            client1.start();
            client.start();
            client2.start();
            client3.start();

            TimeUnit.SECONDS.sleep(1);
            client.enviar("-1","request");
            TimeUnit.SECONDS.sleep(1);
            client1.enviar("-1","request");
            TimeUnit.SECONDS.sleep(1);
            client2.enviar("-1","request");
            TimeUnit.SECONDS.sleep(5);
            client3.enviar("-1","request");
            TimeUnit.MILLISECONDS.sleep(100);
            client.enviar("-1","request");
            TimeUnit.SECONDS.sleep(1);



            } catch (InterruptedException e) {
                e.printStackTrace();
        } finally {
            if (s != null) s.close();
        }
    }
}