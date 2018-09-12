package sample;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static sample.Main.ADDRESS;
import static sample.Main.PORT;


/*
    [SERVER] Esta classe é utilizada como indexição dos recursos e do group
    com o objetivo de não perder as informações caso todos os clientes se desconectarem
 */
public class Server {
    MulticastSocket ms;
    InetAddress group;
    String name;
    Manager recursos;

    // Esse método tem a função de setar as configurações usadas no serviço
    public void configurar(InetAddress group, String name, Manager recursos) {
        this.group = group;
        this.name = name;
        this.recursos = recursos;
        }


        // Cria o grupo multicast para repassar aos demais que desejam entrar nele
    public InetAddress criarGrupo()
    {
        try {
            this.group = InetAddress.getByName(ADDRESS);
            ms = new MulticastSocket(PORT);
            ms.joinGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.group;
    }



}
