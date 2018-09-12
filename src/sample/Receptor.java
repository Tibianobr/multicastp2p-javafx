package sample;

import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

import static sample.Main.TIMEOUT;

public class Receptor extends Thread {
    private byte[] buffer = new byte[10000];
    MulticastSocket ms;
    private String name;
    private Client client;
    private int count_RELEASED = 0;
    private int count_HELD = 0;
    private int num_clientes_checados = 0;
    private Recurso current = null;
    private List<String> lista_respostas = new ArrayList<>();


    public Receptor(MulticastSocket ms, String name, Client client) {
        this.ms = ms;
        this.name = name;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            escutar();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private void escutar() throws GeneralSecurityException {

        while (true) {
            if (this.client.stopWatch.getTime() > TIMEOUT) {
                lista_respostas.add(this.client.name);
                System.out.println(client.name + " deu TIMEOUT em " + CollectionUtils.disjunction(client.ids_conectados.keySet(), lista_respostas));
                for (String nome : CollectionUtils.disjunction(client.ids_conectados.keySet(), lista_respostas)) {
                    client.ids_conectados.remove(nome);
                }
                client.stopWatch.reset();
            }
            current = null;
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            try {
                ms.receive(messageIn);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject retorno = new JSONObject((new String(messageIn.getData())));
            switch (retorno.get("type").toString()) {
                case "conexao":
                    this.conexao(retorno);
                    break;
                case "desconexao":
                    this.desconexao(retorno);
                    break;
                case "freedom":
                    this.freedom(retorno);
                    break;
                case "request":
                    this.request(retorno);
                    break;
                case "response":
                    this.response(retorno);
                    break;
                case "discard":
                    System.out.println(retorno);
                    break;
            }
        }
    }

    private void conexao(JSONObject retorno) throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException {
        JSONArray jsonArray = retorno.getJSONArray("key");
        byte[] bytes_key = new byte[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            bytes_key[i] = (byte) (((int) jsonArray.get(i)) & 0xFF);
        }
        if (!client.ids_conectados.containsKey(retorno.get("id").toString())) {
            client.ids_conectados.put(retorno.get("id").toString(), bytes_key);
            client.enviar("confirmation", "conexao");
        }
    }

    private void desconexao(JSONObject retorno) {
        System.out.println("[DISCONNECT]" + this.client.name + " recebeu o aviso de desconexao de " + retorno.get("id"));
        this.client.ids_conectados.remove(retorno.get("id"));
    }

    private void freedom(JSONObject retorno) throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException {
        if (this.client.status.equals("WANTED")) {
            this.client.enviar(client.protocol_time.toString(), "request");
            num_clientes_checados = 0;
        }
    }

    private void request(JSONObject retorno) throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException {
        if (!retorno.get("id").equals(client.name)) {
            try {
                System.out.println("[REQUEST] " + retorno.get("id") + " solicitou um recurso para " + client.name);
                System.out.println("[CRIPTOGRAFIA] " + this.name + " está desencriptando com a chave pública de " + retorno.get("id"));
                retorno.put("request", Criptografia.desencriptar(Criptografia.loadPublicKey(client.ids_conectados.get(retorno.get("id"))), retorno.getString("request")));
            } catch (Exception e) {
                System.out.println("ERRO DE ASSINATURA DIGITAL");
            }
            this.client.enviar(new JSONObject(retorno.get("request").toString()).get("protocol").toString(), "response");
        }
    }

    private void response(JSONObject retorno) throws GeneralSecurityException {
        if (!retorno.get("id").equals(client.name) && !client.status.equals("HELD")) {
            System.out.println("[CRIPTOGRAFIA] " + this.name + " está desencriptando com a chave pública de " + retorno.get("id"));
            retorno.put("response", Criptografia.desencriptar(Criptografia.loadPublicKey(client.ids_conectados.get(retorno.get("id"))), retorno.getString("response")));
            if (new JSONObject(retorno.get("response").toString()).get("protocol").equals(client.protocol)) {
                num_clientes_checados++;
                lista_respostas.add(retorno.get("id").toString());
                 if (new JSONObject(retorno.get("response").toString()).get("status").equals("WANTED"))
                    System.out.println("[STATUS] " + retorno.get("id") + " se encontra no status WANTED");
                else if (new JSONObject(retorno.get("response").toString()).get("status").equals("RELEASED")) {
                    count_RELEASED++;
                    System.out.println("[RESPOSTA] " + retorno.get("id") + " respondeu o protocolo para " + client.name + " com " + new JSONObject(retorno.get("response").toString()).get("status"));
                    if (count_RELEASED == client.ids_conectados.size() - 1) {
                        if (this.client.stopWatch.isStarted())
                            this.client.stopWatch.suspend();
                        count_RELEASED = 0;
                        current = client.recursos.getfirstFree();
                        if (current != null)
                            current.utilizarRecurso(client, 3000);
                        lista_respostas.clear();
                        client.protocol = -1L;

                    }
                } else if (new JSONObject(retorno.get("response").toString()).get("status").equals("HELD")) {
                    count_HELD++;
                    System.out.println("[RESPOSTA] " + retorno.get("id") + " respondeu o protocolo para " + client.name + " com " + new JSONObject(retorno.get("response").toString()).get("status"));
                    if (count_HELD == client.recursos.size) {
                        count_HELD = 0;
                        count_RELEASED = 0;
                        if (!this.client.status.equals("HELD"))
                            this.client.status = "WANTED";
                        lista_respostas.clear();
                        this.client.stopWatch.suspend();
                        this.client.protocol_time = System.currentTimeMillis();
                    }
                }
                if (num_clientes_checados == client.ids_conectados.size() - 1 && !this.client.status.equals("HELD") && client.protocol != -1) {
                    if (this.client.stopWatch.isStarted()) {
                        this.client.stopWatch.reset();
                    }
                        num_clientes_checados = 0;
                    current = client.recursos.getfirstFree();
                    if (current != null)
                        current.utilizarRecurso(client, 10000);
                    lista_respostas.clear();
                    client.protocol = -1L;
                    count_HELD = 0;
                    count_RELEASED = 0;
                }
            }
        }
    }
}