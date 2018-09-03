package sample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import static sample.Main.ADDRESS;
import static sample.Main.PORT;

public class Simulador {
    //  args give message contents and destination multicast group (e.g. "228.5.6.7")

    void simular() throws IOException {
        MulticastSocket s = null;
        try {
            Server servidor = new Server();
            InetAddress group = servidor.criarGrupo();
            servidor.configurar(group,"Server 01");

            Client client1 = new Client(group, "client01");
            Client client = new Client(group, "client00");
            Client client2 = new Client(group, "client02");

            servidor.start();
            client1.start();
            client.start();
            client2.start();

          //  s.leaveGroup(group);
//        } catch (SocketException e) {
//            System.out.println("Socket: " + e.getMessage());
//        } catch (IOException e) {
//            System.out.println("IO: " + e.getMessage());
        } finally {
            if (s != null) s.close();
        }
    }
}
