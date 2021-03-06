package sample;

import org.json.JSONObject;

/*
    [REQUEST] Outra classe auxiliar, utilizamos ela para padronizar os requests com os campos que gostariamos
 */
public class Request {
    private Long protocol;
    private String criador;
    private Long protocol_time;
    private Integer tempo;


    // A unica pecualiridade que implementamos essa função que devolve uma string formatada como json para já enviarmos
    // da maneira que gostariamos para o pacote
    public String toString()
    {
        JSONObject json = new JSONObject();
        this.protocol = System.currentTimeMillis();
        json.put("protocol", this.protocol);
        json.put("criador", criador);
        json.put("protocol_time", protocol_time);
        json.put("tempo",tempo);
        return json.toString();
    }

    public Request(String criador, Long protocol_time, Integer tempo) {
        this.criador = criador;
        this.protocol_time = protocol_time;
        this.tempo = tempo;
    }


    public String getCriador() {
        return criador;
    }

    public void setCriador(String criador) {
        this.criador = criador;
    }

    public Long getProtocol_time() {
        return protocol_time;
    }

    public void setProtocol_time(Long protocol_time) {
        this.protocol_time = protocol_time;
    }

    public Integer getTempo() {
        return tempo;
    }

    public void setTempo(Integer tempo) {
        this.tempo = tempo;
    }

    public Long getProtocol() {
        return protocol;
    }

    public void setProtocol(Long protocol) {
        this.protocol = protocol;
    }
}
