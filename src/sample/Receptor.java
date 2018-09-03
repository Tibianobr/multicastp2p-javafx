package sample;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class Receptor extends Thread {
    byte[] buffer = new byte[10000];
    MulticastSocket ms;
    String name;
    Client client;

    public Receptor(MulticastSocket ms, String name, Client client)
    {
        this.ms = ms;
        this.name = name;
        this.client = client;
    }

    @Override
    public void run() {
        escutar();
    }

    private void escutar() {
        while (true) {
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            try {
                ms.receive(messageIn);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject retorno = new JSONObject((new String(messageIn.getData())));
            if(retorno.get("type").equals("conexao")) {
                client.ids_conectados.add(retorno.get("id").toString());
            }
       //     System.out.println(name + " Received: " + new JSONObject((new String(messageIn.getData()))).get("msg"));

            try {
                this.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(client.name + " conhece " + client.ids_conectados);
        }
    }
}
