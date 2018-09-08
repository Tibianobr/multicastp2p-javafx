package sample;

import org.json.JSONObject;

public class Response {
    private Long protocol;
    private String response;


    public String toString()
    {
        JSONObject json = new JSONObject();
        json.put("protocol",this.protocol);
        json.put("response",this.response);
        return json.toString();
    }

    public Response(Long protocol, String response) {
        this.protocol = protocol;
        this.response = response;
    }

    public Long getProtocol() {
        return protocol;
    }

    public void setProtocol(Long protocol) {
        this.protocol = protocol;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
