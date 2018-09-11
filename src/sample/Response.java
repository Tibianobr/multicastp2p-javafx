package sample;

import org.json.JSONObject;

public class Response {
    private Long protocol;
    private String status;


    public String toString()
    {
        JSONObject json = new JSONObject();
        json.put("protocol",this.protocol);
        json.put("status",this.status);
        return json.toString();
    }

    public Response(Long protocol, String response) {
        this.protocol = protocol;
        this.status = response;
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
}
