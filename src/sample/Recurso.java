package sample;

public class Recurso{
    private String name;
    private Integer status;
    private Client current;


    public Recurso(String name, Integer status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Client getCurrent() {
        return current;
    }

    public void setCurrent(Client current) {
        this.current = current;
    }
}
