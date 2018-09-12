package sample;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

    public String utilizarRecurso(Client cliente, Integer time)
    {
        this.setCurrent(cliente);
        System.out.println("[USAGE] " + current.name + " está utilizando o recurso " + this.id_name);
        this.status = BUSY;
        current.status = "HELD";
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                            sairDoRecurso();
                        } catch (GeneralSecurityException e) {
                            e.printStackTrace();
                        }
                    }
                },
                time
        );
        return this.id_name;
    }

    public void sairDoRecurso() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException {
        this.current.status = "RELEASED";
        System.out.println("[FREEDOM] "+ current.name + " parou de utilizar o recurso " + this.id_name);
        this.current.enviar("","freedom");
        this.setCurrent(null);
        this.status = WAITING;
    }
}
