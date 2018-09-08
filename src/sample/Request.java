package sample;

import org.json.JSONObject;

public class Request {
    private Long protocol;
    private String criador;
    private String recurso;
    private Integer tempo;



    public String toString()
    {
        JSONObject json = new JSONObject();
        this.protocol = System.currentTimeMillis();
        json.put("protocol", this.protocol);
        json.put("criador", criador);
        json.put("recurso", recurso);
        json.put("tempo",tempo);
        return json.toString();
    }

    public Request(String criador, String recurso, Integer tempo) {
        this.criador = criador;
        this.recurso = recurso;
        this.tempo = tempo;
    }


    public String getCriador() {
        return criador;
    }

    public void setCriador(String criador) {
        this.criador = criador;
    }

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
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
