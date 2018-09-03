package sample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

import static java.lang.System.currentTimeMillis;
import static sample.Main.PORT;

public class Client extends Thread {
    MulticastSocket ms;
    InetAddress group;
    String name;
    Receptor receptor;
    List<String> ids_conectados;



    public Client(InetAddress group, String name) {
        this.group = group;
        this.name = name;
        try {
            this.ms = new MulticastSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.ids_conectados = new ArrayList<>();
        this.receptor = new Receptor(ms, name,this);
        this.receptor.start();
    }

    private void joinGroup() throws IOException {
        ms.joinGroup(group);
        enviar(this.name + " entrou no grupo!","conexao");
    }


    //JSON como padrão de transferencia de mensagens
    @Override
    public void run() {
        JSONObject my_obj = new JSONObject();
        my_obj.put("name", name);
        try {
            joinGroup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviar(String msg, String tipo) {
        JSONObject json = new JSONObject();
        json.put("msg", msg);
        json.put("type", tipo);
        json.put("id", this.name);
        json.put("time",currentTimeMillis());
        DatagramPacket messageOut = new DatagramPacket(json.toString().getBytes(), json.toString().getBytes().length, group, PORT);
        try {
            ms.send(messageOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
