package sample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import static sample.Main.ADDRESS;
import static sample.Main.PORT;

public class Server extends Thread {
    MulticastSocket ms;
    InetAddress group;
    String name;
    byte[] buffer = new byte[1000];

    public void configurar(InetAddress group, String name) {
        this.group = group;
        this.name = name;
        }

    @Override
    public void run() {
             while (true) {
        DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
        try {
            ms.receive(messageIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Received:" + new String(messageIn.getData()));
        try {
            System.out.println("sleep");
            this.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    }

    public InetAddress criarGrupo()
    {
        try {
            this.group = InetAddress.getByName(ADDRESS);
            ms = new MulticastSocket(PORT);
            ms.joinGroup(group);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return this.group;
    }

//     while (true) {
//        DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
//        try {
//            ms.receive(messageIn);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Received:" + new String(messageIn.getData()));
//        try {
//            System.out.println("sleep");
//            this.sleep(1500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }


}
