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

/*
    [CLIENT] Peça principal do sistema, vai ser detalhado no decorrer do código abaixo.
    Possui as maiores iterações com o sistema e também o maior conhecimendo do "grupo"
 */

public class Client extends Thread {
    // Esse bloco abaixo é padrão para qualquer peça do grupo multicast
    private MulticastSocket ms;
    private InetAddress group;
    // Identificador do cliente (Utilizamos um nome, mas poderia ser um ID)
    String name;
    // O receptor é um dos segredos do cliente, iremos aprofundar mais na classe RECEPTOR
    // cada cliente possui seu receptor para sempre escutar as mensagens
    private Receptor receptor;
    // Lista de chaves e dos clientes conectados na rede
    Map<String, byte[]> ids_conectados;
    // As chaves do cliente ficam no keyring que é uma classe GENERATEKEYS
    private GenerateKeys keyring;
    // Sincronizador inicial, "portão" que libera o cliente para executar
    private CyclicBarrier gate;
    // Para não ter acesso direto aos recursos cada cliente possui o conhecimento do gerenciador de recursos do grupo
    // que executa o acesso aos recursos do cliente
    Manager recursos;
    // Situação em que se encontra o cliente
    String status;
    // Protocolo atual do cliente
    Long protocol;
    // Tempo em que foi gerado o protocolo
    Long protocol_time;
    // Cronometro pessoal do cliente para controlar timeouts
    StopWatch stopWatch;


    // Ao iniciar o cliente são passadas algumas informações do grupo
    // Iniciadas algumas váriaveis do próprio cliente
    // e geradas as chaves dele mesmo
    // e finaliza inicial o Receptor(Listener) dele
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

    // Função que coloca o cliente no grupo e envia uma mensagem de conexao aos demais
    private void joinGroup() throws IOException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException {
        ms.joinGroup(group);
        enviar(this.name + " entrou no grupo!", "conexao");
    }



    // O run é utilizado quando damos o .start() na Thread
    // basicamente checamos se precisa de sincronização e juntamos o cliente no grupo
    @Override
    public void run() {
        try {
            if (gate != null)
                gate.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            ex.printStackTrace();
        }
        try {
            joinGroup();
        } catch (IOException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    // A segunda peça chave do cliente é essa função
    // Ela possui diversos tipos de mensagens que vem como segundo parametro
    // e dependendo da mensagem ela trabalha o pacote de uma maneira
    // estamos utilizando o JSON como forma de comunicação já que ele é serializavel para bytes e facilita o acesso
    public void enviar(String msg, String tipo) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException {
        Request r;
        JSONObject json = new JSONObject();
        // Todas as mensagens tem um cabeçalho base para identificação do pacote
        // Ele possui a mensagem, tipo, identificador de quem criou e o tempo que foi criado o pacote
        json.put("msg", msg);
        json.put("type", tipo);
        json.put("id", this.name);
        json.put("time", currentTimeMillis());
        // A partir disso trabalhamos os tipos especificos do pacote
        // Caso seja do tipo conexao, iremos enviar também a chave pública para os demais armazenarem ela
        if (tipo.equals("conexao"))
            json.put("key", this.keyring.getPublicKey().getEncoded());
        // Caso seja do tipo request(solicitação de recurso) o processo tem que garantir que não esteja já utilizando
        // passamos também o tempo que o protocolo foi criado para priorizar quem pediu antes caso exista mais de um
        // processo com o estado de WANTED
        else if (tipo.equals("request") && !this.status.equals("HELD")) {
            if (Long.parseLong(msg) != -1) {
                r = new Request(this.name, Long.parseLong(msg), 200);
            }
            else {
                this.protocol_time = System.currentTimeMillis();
                r  = new Request(this.name, protocol_time, 200);
            }
            // Criptografamos uma parte do pacote com a chave privada de quem está enviando
            // para assim garantir a assinatura digital
            json.put("request", Criptografia.encriptar(keyring.getPrivateKey(),r.toString()));
            System.out.println("[CRIPTOGRAFIA] " + this.name + " está criptografando com sua chave privada o [REQUEST]");
            this.protocol = r.getProtocol();
            // Inicializamos o timer de timeout e caso já tivessemos enviado reiniciamos a contagem
            if (!this.stopWatch.isStarted())
                this.stopWatch.start();
            else {
                this.stopWatch.reset();
                this.stopWatch.start();
            }
            // Colocamos o cliente como desejando acessar o recurso
            this.status = "WANTED";
            }
            // Para a resposta(reponse) seguimos a mesma logica de criptografar uma parte da mensagem
            else if (tipo.equals("response")) {
            json.put("response", Criptografia.encriptar(keyring.getPrivateKey(),new Response(Long.parseLong(msg), this.status,this.protocol_time).toString()));
            System.out.println("[CRIPTOGRAFIA] " + this.name + " está criptografando com sua chave privada a [RESPONSE]");
        }
        // Caso o client esteja fazendo um request que não faz sentido
        // Como por exemplo pedir o recurso enquanto está utilizando ou desconectar enquanto está com o recurso
        // somente discartamos a mensagem
        else if(tipo.equals("request") && status.equals("HELD") || tipo.equals("desconexao") && status.equals("HELD"))
            json.put("type","discard");
        // Finalmente enviamos a mensagem
        DatagramPacket messageOut = new DatagramPacket(json.toString().getBytes(), json.toString().getBytes().length, group, PORT);
        try {
            ms.send(messageOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ao tentar sair do grupo o cliente informa os demais enviando uma mensagem do tipo desconexao
    // e sai do grupo multicast
    public void leaveGroup() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException {
        this.enviar("", "desconexao");
        try {
            this.ms.leaveGroup(this.group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
