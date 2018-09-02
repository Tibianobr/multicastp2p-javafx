package sample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import static sample.Main.PORT;

public class Client extends Thread {
    MulticastSocket ms;
    InetAddress group;
    String name;

    public Client(InetAddress group,String name) {
        this.group = group;
        this.name = name;
        try {
            this.ms = new MulticastSocket(PORT);
            this.ms.joinGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        for (int i=0;i<3;i++){
        DatagramPacket messageOut = new DatagramPacket(name.getBytes(), name.getBytes().length, group, PORT);
        try {
            ms.send(messageOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    }

//    for (int i=0;i<3;i++){
//        DatagramPacket messageOut = new DatagramPacket(name.getBytes(), name.getBytes().length, group, this.port);
//        try {
//            ms.send(messageOut);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            this.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
