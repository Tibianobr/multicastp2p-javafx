package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Main;
import sample.Simulador;

import java.security.GeneralSecurityException;
import static sample.Main.TIMEOUT;


public class ConfigController {

    @FXML
    private TextField tempoResposta;

    public void changeScreen() throws GeneralSecurityException {
        System.out.println("OK");
        Main.changeScreen("Sample");
        TIMEOUT = Integer.parseInt(tempoResposta.getText());
        new Simulador().start();
    }
}
