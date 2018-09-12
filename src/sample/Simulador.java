package sample;

import controllers.SampleController;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static sample.Main.WAITING;

/*
    [SIMULADOR] esta classe é responsável por implementar uma simulação de um grupo multicast e suas ações
    Podendo ser tanto visual com auxilio das telas, quanto automatico com uma situação pré estabelecida
    Os passos para isso:
    - Criar um servidor de indexição para criação do grupo multi-cast;
    - O servidor cria o grupo multicast;
    - São criados os recursos que serão utilizados na simulação;
    - Um manager dos recursos disponíveis é atribuido a cada nó, para conhecerem os recursos disponíveis;
    - Utilizamos o CyclicBarrier para sincronizar as threads dos clientes simulando que eles entraram ao mesmo tempo;
    - Efetuamos alguns requests com um certo espaçamento para poupar processamento
 */
public class Simulador extends Thread {
    public boolean automatico;

    public Simulador(boolean automatico) {
        this.automatico = automatico;
    }

    @Override
    public void run() {
        try {
            if (automatico)
                simular();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }


    public void simular() throws GeneralSecurityException {
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
            client.enviar("-1", "request");
            TimeUnit.SECONDS.sleep(1);
            client1.enviar("-1", "request");
            TimeUnit.SECONDS.sleep(1);
            client2.enviar("-1", "request");
            TimeUnit.SECONDS.sleep(5);
            client3.enviar("-1", "request");
            TimeUnit.MILLISECONDS.sleep(100);
            client.enviar("-1", "request");
            TimeUnit.SECONDS.sleep(1);

            client2.leaveGroup();

            TimeUnit.SECONDS.sleep(5);
            Client client4 = new Client(group, "Cliente X", null, manager);
            client4.start();
            TimeUnit.SECONDS.sleep(5);
            client4.enviar("-1", "request");
            TimeUnit.SECONDS.sleep(5);
            client4.leaveGroup();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}