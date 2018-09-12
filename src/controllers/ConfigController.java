package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Main;
import sample.Simulador;

import javax.swing.*;
import java.security.GeneralSecurityException;
import static sample.Main.TIMEOUT;


public class ConfigController {

    @FXML
    private TextField tempoResposta;

    public void changeScreen() throws GeneralSecurityException {
        if(tempoResposta.getText().isEmpty())
            JOptionPane.showMessageDialog(null, "Favor inserir o tempo");
        else{
            Main.changeScreen("Sample");
            TIMEOUT = Integer.parseInt(tempoResposta.getText());
            new Simulador(false).start();
        }
    }
}
