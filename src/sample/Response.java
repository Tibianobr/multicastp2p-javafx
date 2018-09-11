package sample;

import org.json.JSONObject;

public class Response {
    private Long protocol;
    private String status;
    private Long protocol_time;


    public String toString()
    {
        JSONObject json = new JSONObject();
        json.put("protocol",this.protocol);
        if(this.status.equals("WANTED"))
            json.put("protocol_time", protocol_time);
        json.put("status",this.status);
        return json.toString();
    }

    public Response(Long protocol, String status, Long protocol_time) {
        this.protocol = protocol;
        this.status = status;
        this.protocol_time = protocol_time;
    }

    public Long getProtocol() {
        return protocol;
    }

    public void setProtocol(Long protocol) {
        this.protocol = protocol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String response) {
        this.status = response;
    }

    public Long getProtocol_time() {
        return protocol_time;
    }

    public void setProtocol_time(Long protocol_time) {
        this.protocol_time = protocol_time;
    }
}
