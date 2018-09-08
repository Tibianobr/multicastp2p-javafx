package sample;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sample.Main.ADDRESS;
import static sample.Main.PORT;

public class Server extends Thread {
    MulticastSocket ms;
    InetAddress group;
    String name;
    byte[] buffer = new byte[10000];
    Map<String,String> ids_conectados;
    List<Recurso> recursos;

    public void configurar(InetAddress group, String name, List<Recurso> recursos) {
        this.group = group;
        this.name = name;
        this.ids_conectados = new HashMap<>();
        this.recursos = recursos;
        }

    @Override
    public void run() {
        int i = 0;
             while (i < 10) {
        DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
        try {
            ms.receive(messageIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject retorno = new JSONObject((new String(messageIn.getData())));
        if(retorno.get("type").equals("conexao")) {
            ids_conectados.put(retorno.get("id").toString(),retorno.get("key").toString());
        }
       // System.out.println("Server Received: " + new JSONObject((new String(messageIn.getData()))).get("msg"));
        try {
            sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        i++;
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



}
