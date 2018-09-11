package sample;

import org.json.JSONObject;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static sample.Main.WAITING;

public class Simulador {

    void simular() {
        MulticastSocket s = null;
        try {
            Server servidor = new Server();
            InetAddress group = servidor.criarGrupo();
            Recurso processador = new Recurso("Recurso 001", WAITING);
            Recurso memoria = new Recurso("Recurso 002", WAITING);
            List<Recurso> recursos = Arrays.asList(processador, memoria);
            Manager manager = new Manager(recursos);

            servidor.configurar(group, "Server 01", manager);

            final CyclicBarrier initial_gate = new CyclicBarrier(3);
            Client client1 = new Client(group, "Cliente A", initial_gate, manager);
            Client client = new Client(group, "Cliente B", initial_gate, manager);
            Client client2 = new Client(group, "Cliente C", initial_gate, manager);

         //   servidor.start();
            TimeUnit.MILLISECONDS.sleep(500);
            client1.start();
            TimeUnit.MILLISECONDS.sleep(1200);
            client.start();
            TimeUnit.MILLISECONDS.sleep(1200);
            client2.start();

            TimeUnit.MILLISECONDS.sleep(1200);
            client.enviar("Recurso 001","request");

            client2.leaveGroup();

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