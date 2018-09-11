package sample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.json.*;

import static java.lang.System.currentTimeMillis;
import static sample.Main.PORT;

public class Client extends Thread {
    MulticastSocket ms;
    InetAddress group;
    String name;
    Receptor receptor;
    Map<String, String> ids_conectados;
    GenerateKeys keyring;
    CyclicBarrier gate;
    Manager recursos;
    String status;
    Long protocol;


    public Client(InetAddress group, String name, CyclicBarrier gate, Manager recursos) {
        this.group = group;
        this.name = name;
        this.recursos = recursos;
        this.status = "RELEASED";
        try {
            this.ms = new MulticastSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.ids_conectados = new HashMap<>();
        this.gate = gate;
        try {
            keyring = new GenerateKeys(1024);
            keyring.createKeys();
            // System.out.println(gk.getPrivateKey().getEncoded());
            //System.out.println("CRIADA = " + Arrays.toString(keyring.getPrivateKey().getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        this.receptor = new Receptor(ms, name, this);
        this.receptor.start();
    }

    private void joinGroup() throws IOException {
        ms.joinGroup(group);
        enviar(this.name + " entrou no grupo!", "conexao");
    }


    //JSON como padrão de transferencia de mensagens
    @Override
    public void run() {
        try {
            gate.await();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (BrokenBarrierException ex) {
            ex.printStackTrace();
        }
        JSONObject my_obj = new JSONObject();
        my_obj.put("name", name);
        try {
            joinGroup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviar(String msg, String tipo) {
        // TODO CRIPTOGRAFIA NA MENSAGEM COMO ASSINATURA DIGITAL
        JSONObject json = new JSONObject();
        json.put("msg", msg);
        json.put("type", tipo);
        json.put("id", this.name);
        json.put("time", currentTimeMillis());
        if (tipo.equals("conexao"))
            json.put("key", this.keyring.getPublicKey().getEncoded());
        else if (tipo.equals("request")) {
            Request r = new Request(this.name, msg, 200);
            json.put("request", r.toString());
            this.status = "WANTED";
            this.protocol = r.getProtocol();
        } else if (tipo.equals("response")) {
            json.put("response", new Response(Long.parseLong(msg), this.status).toString());
        }
        DatagramPacket messageOut = new DatagramPacket(json.toString().getBytes(), json.toString().getBytes().length, group, PORT);
        try {
            ms.send(messageOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
