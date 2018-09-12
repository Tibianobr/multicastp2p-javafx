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
import java.util.concurrent.TimeUnit;

import static sample.Main.SAMPLECONTROLLER;
import static sample.Main.TIMEOUT;

/*
    [RECEPTOR] Classe chave do sistema, responsável por escutar e interpretar as mensagens
 */

public class Receptor extends Thread {
    // Temos novamente o bloco basico de quem lida com multicast
    private byte[] buffer = new byte[10000];
    MulticastSocket ms;
    private String name;
    // O receptor conhece seu cliente
    private Client client;
    // Variaveis auxiliares para controle de acesso
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

    // A partir do momento que ele é iniciado ele fica escutando
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


            current = null;
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            // Recebe a mensagem
            try {
                ms.receive(messageIn);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Cria um json com o conteúdo da mensagem
            JSONObject retorno = new JSONObject((new String(messageIn.getData())));

            // Analisa o tipo da mensagem para fazer a interpretação
            // Com exceção do discard as mensagens serão tratadas por funções
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
                case "timeout":
                    this.timeoutcheck(retorno);
                    break;
                case "discard":
                    System.out.println("[DISCARD] o grupo descartou a mensagem de " + retorno.get("id") + " pois fez um request ou desconexao enquanto utiliza o recurso!! ");
                    SAMPLECONTROLLER.atualizarLog("[DISCARD] o grupo descartou a mensagem de " + retorno.get("id") + " pois fez um request ou desconexao enquanto utiliza o recurso!! ");
                    break;
            }
            // Trata o Timeout iniciado ao mandar um request
            if (this.client.stopWatch.getTime() > TIMEOUT) {
                lista_respostas.add(this.client.name);
                String timeout = "";
                System.out.println("[TIMEOUT]" + client.name + " deu TIMEOUT em " + CollectionUtils.disjunction(client.ids_conectados.keySet(), lista_respostas));
                for (String nome : CollectionUtils.disjunction(client.ids_conectados.keySet(), lista_respostas)) {
                     client.ids_conectados.remove(nome);
                     timeout = nome;
                }
                client.enviar(timeout,"timeout");
                client.stopWatch.reset();
            }
        }
    }

    private void timeoutcheck(JSONObject retorno) throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException {
        if(retorno.get("timeout").equals(this.client.name))
            this.client.enviar("","conexao");
    }

    /*
    A função conexão irá receber a chave pública enviada na mensagem e deserializar para o byte array para preservar
    o valor dela, caso o cliente ainda não possua o conhecimento dessa chave do cliente ele armazena na sua lista
    e envia uma confirmação que recebeu a mensagem de conexao
     */
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

    // Um caso simples é o de desconexao que ao receber somente retira o cliente da lista de conectados
    private void desconexao(JSONObject retorno) {
        System.out.println("[DISCONNECT]" + this.client.name + " recebeu o aviso de desconexao de " + retorno.get("id"));
        SAMPLECONTROLLER.atualizarLog("[DISCONNECT]" + this.client.name + " recebeu o aviso de desconexao de " + retorno.get("id"));
        this.client.ids_conectados.remove(retorno.get("id"));
    }

    // O freedom acontece quando um cliente para de utilizar o recurso
    // ao escutar isso o cliente que está wanted vai reenviar o request para solicitar o recurso que ficou livre
    private void freedom(JSONObject retorno) throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException {
        if (this.client.status.equals("WANTED")) {
            this.client.enviar(client.protocol_time.toString(), "request");
            num_clientes_checados = 0;
        }
    }

    /*
        Ao escutar um request o receptor vai desencriptar com a chave de quem disse que mandou
        e caso haja sucesso ele envia sua situação com o numero de protocolo do request
        para evitar que haja interpretações erroneas de respostas
     */
    private void request(JSONObject retorno) throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException {
        if (!retorno.get("id").equals(client.name)) {
            try {
                System.out.println("[REQUEST] " + retorno.get("id") + " solicitou um recurso para " + client.name);
                System.out.println("[CRIPTOGRAFIA] " + this.name + " está desencriptando com a chave pública de " + retorno.get("id"));
                SAMPLECONTROLLER.atualizarLog("[REQUEST] " + retorno.get("id") + " solicitou um recurso para " + client.name);
                SAMPLECONTROLLER.atualizarLog("[CRIPTOGRAFIA] " + this.name + " está desencriptando com a chave pública de " + retorno.get("id"));
                retorno.put("request", Criptografia.desencriptar(Criptografia.loadPublicKey(client.ids_conectados.get(retorno.get("id"))), retorno.getString("request")));
            } catch (Exception e) {
                System.out.println("ERRO DE ASSINATURA DIGITAL");
                SAMPLECONTROLLER.atualizarLog("ERRO DE ASSINATURA DIGITAL");
            }
            this.client.enviar(new JSONObject(retorno.get("request").toString()).get("protocol").toString(), "response");
        }
    }

    /*
        A interpretação da resposta é a mais extensa pois possui diversos tipos
        Dependendo da quantidade de clientes que já responderam o protocolo de interesse ele pode acessar o recurso ou
        entrar no status de WANTED
     */
    private void response(JSONObject retorno) throws GeneralSecurityException {
        if (client.ids_conectados.containsKey(retorno.get("id")))
        {
        // Checamos se interessa a mensagem para o receptor
        if (!retorno.get("id").equals(client.name) && !client.status.equals("HELD")) {
            // Desencriptamos de maneira semelhante as outras
            System.out.println("[CRIPTOGRAFIA] " + this.name + " está desencriptando com a chave pública de " + retorno.get("id"));
            SAMPLECONTROLLER.atualizarLog("[CRIPTOGRAFIA] " + this.name + " está desencriptando com a chave pública de " + retorno.get("id"));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retorno.put("response", Criptografia.desencriptar(Criptografia.loadPublicKey(client.ids_conectados.get(retorno.get("id"))), retorno.getString("response")));
            // Caso o protocolo da mensagem interesse pra gente iremos começar a interpretar as respostas
            if (new JSONObject(retorno.get("response").toString()).get("protocol").equals(client.protocol)) {
                // Iremos sempre checar se o numero de clientes que responderam já não foi o suficiente
                num_clientes_checados++;
                // Também anotamos quem foi para o TIMEOUT
                lista_respostas.add(retorno.get("id").toString());
                // Caso a resposta esteja WANTED iremos tratar com o reenvio no FREEDOM
                 if (new JSONObject(retorno.get("response").toString()).get("status").equals("WANTED"))
                    System.out.println("[STATUS] " + retorno.get("id") + " se encontra no status WANTED");
                 // Caso a resposta seja positiva armazenamos ela e checamos novamente se não atingimos o suficiente
                else if (new JSONObject(retorno.get("response").toString()).get("status").equals("RELEASED")) {
                    count_RELEASED++;
                    System.out.println("[RESPOSTA] " + retorno.get("id") + " respondeu o protocolo para " + client.name + " com " + new JSONObject(retorno.get("response").toString()).get("status"));
                    SAMPLECONTROLLER.atualizarLog("[RESPOSTA] " + retorno.get("id") + " respondeu o protocolo para " + client.name + " com " + new JSONObject(retorno.get("response").toString()).get("status"));
                     // Caso seja suficiente, libera o acesso ao recurso e zera o TIMEOUT
                    if (count_RELEASED == client.ids_conectados.size() - 1) {
                        if (this.client.stopWatch.isStarted())
                            this.client.stopWatch.suspend();
                        count_RELEASED = 0;
                        current = client.recursos.getfirstFree();
                        if (current != null)
                            current.utilizarRecurso(client, client.request_time);
                        lista_respostas.clear();
                        client.protocol = -1L;

                    }
                }
                // Caso seja negativa, armazenamos também e vemos se todos os recursos já não estão ocupados
                 // caso estejam já deixamos o cliente como wanted, pois não conseguirá o recursos ainda
                else if (new JSONObject(retorno.get("response").toString()).get("status").equals("HELD")) {
                    count_HELD++;
                    System.out.println("[RESPOSTA] " + retorno.get("id") + " respondeu o protocolo para " + client.name + " com " + new JSONObject(retorno.get("response").toString()).get("status"));
                    SAMPLECONTROLLER.atualizarLog("[RESPOSTA] " + retorno.get("id") + " respondeu o protocolo para " + client.name + " com " + new JSONObject(retorno.get("response").toString()).get("status"));
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
                // Caso mesmo com resposta negativas exista um recurso disponível e o número de respostas acabou
                // iremos acessar o recurso e executar novamente o reset do TIMEOUT
                if (num_clientes_checados == client.ids_conectados.size() - 1 && !this.client.status.equals("HELD") && client.protocol != -1) {
                    if (this.client.stopWatch.isStarted()) {
                        this.client.stopWatch.reset();
                    }
                        num_clientes_checados = 0;
                    current = client.recursos.getfirstFree();
                    if (current != null)
                        current.utilizarRecurso(client, client.request_time);
                    lista_respostas.clear();
                    client.protocol = -1L;
                    count_HELD = 0;
                    count_RELEASED = 0;
                }
            }
        }
    }
    else
        if (num_clientes_checados == client.ids_conectados.size() - 1 && !this.client.status.equals("HELD") && client.protocol != -1) {
            if (this.client.stopWatch.isStarted()) {
                this.client.stopWatch.reset();
            }
            num_clientes_checados = 0;
            current = client.recursos.getfirstFree();
            if (current != null)
                current.utilizarRecurso(client, client.request_time);
            lista_respostas.clear();
            client.protocol = -1L;
            count_HELD = 0;
            count_RELEASED = 0;
        }
    }
}