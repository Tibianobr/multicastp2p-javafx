package sample;

import java.util.concurrent.TimeUnit;

import static sample.Main.BUSY;
import static sample.Main.WAITING;

public class Recurso{
    private String id_name;
    private Integer status;
    private Client current;


    public Recurso(String name, Integer status) {
        this.id_name = name;
        this.status = status;
    }


    public String getId_name() {
        return id_name;
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

    public String utilizarRecurso(Client cliente ,Integer time)
    {
        this.setCurrent(cliente);
        this.status = BUSY;
        cliente.status = "BUSY";
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        sairDoRecurso();
                    }
                },
                time
        );
        return this.id_name;
    }

    public void sairDoRecurso()
    {
        this.current.status = "RELEASED";
        this.setCurrent(null);
        this.status = WAITING;
    }


}
