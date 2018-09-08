package sample;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

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
                client.ids_conectados.put(retorno.get("id").toString(),retorno.get("key").toString());
                JSONArray jsonArray = retorno.getJSONArray("key");
                byte[] bytes_key = new byte[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    bytes_key[i]=(byte)(((int)jsonArray.get(i)) & 0xFF);
                }
            }
            else if (retorno.get("type").equals("request") && !retorno.get("id").equals(client.name))
            {
                System.out.println(retorno.get("id").toString() + " com o request = " + retorno.get("request").toString());
                this.client.enviar(new JSONObject(retorno.get("request").toString()).get("protocol").toString(),"response");
            }
            else if (retorno.get("type").equals("response") && !retorno.get("id").equals(client.name) && new JSONObject(retorno.get("response").toString()).get("protocol").equals(client.protocol))
            {
                System.out.println(client.name + " OUVIU " + retorno.get("id").toString() + " respondeu = " + retorno.get("response").toString());
            }
//            if (client.ids_conectados.size() == 3)
//                System.out.println(client.name + " conhece " + client.ids_conectados.keySet());

        }
    }
}

//try {
//        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes_key));
//        String cod = RSACryptography.encrypt(publicKey, "KRL AGORA VAI");
//        System.out.println(RSACryptography.decrypt(client.gk.getPrivateKey(), cod));
//        } catch (InvalidKeySpecException e) {
//        e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//        e.printStackTrace();
//        } catch (BadPaddingException e) {
//        e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//        e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//        e.printStackTrace();
//        } catch (InvalidKeyException e) {
//        e.printStackTrace();
//        }