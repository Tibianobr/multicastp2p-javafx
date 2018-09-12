package sample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.apache.commons.lang3.time.StopWatch;
import org.json.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static java.lang.System.currentTimeMillis;
import static sample.Main.PORT;

public class Client extends Thread {
    private MulticastSocket ms;
    private InetAddress group;
    String name;
    private Receptor receptor;
    Map<String, byte[]> ids_conectados;
    private GenerateKeys keyring;
    private CyclicBarrier gate;
    Manager recursos;
    String status;
    Long protocol;
    Long protocol_time;
    Map<Long,Long> queue;
    StopWatch stopWatch;


    public Client(InetAddress group, String name, CyclicBarrier gate, Manager recursos) {
        this.group = group;
        this.name = name;
        this.recursos = recursos;
        this.status = "RELEASED";
        stopWatch = new StopWatch();
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
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        this.receptor = new Receptor(ms, name, this);
        this.receptor.start();
    }

    private void joinGroup() throws IOException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException {
        ms.joinGroup(group);
        enviar(this.name + " entrou no grupo!", "conexao");
    }


    //JSON como padrão de transferencia de mensagens
    @Override
    public void run() {
        try {
            gate.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            ex.printStackTrace();
        }
        JSONObject my_obj = new JSONObject();
        my_obj.put("name", name);
        try {
            joinGroup();
        } catch (IOException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    public void enviar(String msg, String tipo) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException {
        // TODO CRIPTOGRAFIA NA MENSAGEM COMO ASSINATURA DIGITAL
        Request r = null;
        JSONObject json = new JSONObject();
        json.put("msg", msg);
        json.put("type", tipo);
        json.put("id", this.name);
        json.put("time", currentTimeMillis());
        if (tipo.equals("conexao"))
            json.put("key", this.keyring.getPublicKey().getEncoded());
        else if (tipo.equals("request") && !this.status.equals("HELD")) {
            if (Long.parseLong(msg) != -1) {
                r = new Request(this.name, Long.parseLong(msg), 200);
            }
            else {
                this.protocol_time = System.currentTimeMillis();
                r = new Request(this.name, protocol_time, 200);
            }
            json.put("request", Criptografia.encriptar(keyring.getPrivateKey(),r.toString()));
            this.protocol = r.getProtocol();
            if (!this.stopWatch.isStarted())
                this.stopWatch.start();
            else {
                this.stopWatch.reset();
                this.stopWatch.start();
            }
            this.status = "WANTED";
            } else if (tipo.equals("response")) {
            json.put("response", Criptografia.encriptar(keyring.getPrivateKey(),new Response(Long.parseLong(msg), this.status,this.protocol_time).toString()));
        }
        else if(tipo.equals("request") && status.equals("HELD"))
            json.put("type","discard");
        DatagramPacket messageOut = new DatagramPacket(json.toString().getBytes(), json.toString().getBytes().length, group, PORT);
        try {
            ms.send(messageOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void leaveGroup() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException {
        this.enviar("", "desconexao");
        try {
            this.ms.leaveGroup(this.group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
