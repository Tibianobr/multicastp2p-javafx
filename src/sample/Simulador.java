package sample;

import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static sample.Main.ADDRESS;
import static sample.Main.PORT;

public class Simulador {
    //  args give message contents and destination multicast group (e.g. "228.5.6.7")

    void simular() throws IOException {
        MulticastSocket s = null;
        try {
            Server servidor = new Server();
            InetAddress group = servidor.criarGrupo();
            servidor.configurar(group, "Server 01");

            final CyclicBarrier initial_gate = new CyclicBarrier(3);
            Client client1 = new Client(group, "client01", initial_gate);
            Client client = new Client(group, "client00", initial_gate);
            Client client2 = new Client(group, "client02", initial_gate);

            servidor.start();

//                        GenerateKeys gk = null;
//            try {
//                gk = new GenerateKeys(1024);
//                gk.createKeys();
//                 System.out.println(gk.getPrivateKey().getEncoded());
//                  System.out.println(gk.getPublicKey().getEncoded());
//            } catch (NoSuchAlgorithmException e) {
//                System.err.println(e.getMessage());
//            }
//            JSONObject json = new JSONObject();
//            json.put("key",gk.getPublicKey().getEncoded());
//            System.out.println(json.get("key"));
//            try {
//                String cod = RSACryptography.encrypt(gk.getPrivateKey(), "CRIPTOGRAFIA WOW");
//                System.out.println(cod);
//                PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec( ((byte[]) json.get("key"))));
//                System.out.println(RSACryptography.decrypt(publicKey, cod));
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (InvalidKeyException e) {
//                e.printStackTrace();
//            } catch (NoSuchPaddingException e) {
//                e.printStackTrace();
//            } catch (BadPaddingException e) {
//                e.printStackTrace();
//            } catch (InvalidKeySpecException e) {
//                e.printStackTrace();
//            } catch (IllegalBlockSizeException e) {
//                e.printStackTrace();
//            }
            client1.start();
            TimeUnit.SECONDS.sleep(1);
            client.start();
            TimeUnit.SECONDS.sleep(1);
            client2.start();

            //  s.leaveGroup(group);
//        } catch (SocketException e) {
//            System.out.println("Socket: " + e.getMessage());
//        } catch (IOException e) {
//            System.out.println("IO: " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (s != null) s.close();
        }
    }
}


//
//                        GenerateKeys gk = null;
//            try {
//                gk = new GenerateKeys(1024);
//                gk.createKeys();
//                 System.out.println(gk.getPrivateKey().getEncoded());
//                  System.out.println(gk.getPublicKey().getEncoded());
//            } catch (NoSuchAlgorithmException e) {
//                System.err.println(e.getMessage());
//            }
//            JSONObject json = new JSONObject();
//            json.put("key",gk.getPublicKey().getEncoded());
//            try {
//                String cod = RSACryptography.encrypt(gk.getPrivateKey(), "CRIPTOGRAFIA BOYZ");
//                System.out.println(cod);
//                PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec((byte[]) json.get("key")));
//                System.out.println(RSACryptography.decrypt(publicKey, cod));
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (InvalidKeyException e) {
//                e.printStackTrace();
//            } catch (NoSuchPaddingException e) {
//                e.printStackTrace();
//            } catch (BadPaddingException e) {
//                e.printStackTrace();
//            } catch (InvalidKeySpecException e) {
//                e.printStackTrace();
//            } catch (IllegalBlockSizeException e) {
//                e.printStackTrace();
//            }