package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Main;
import sample.Simulador;

import static sample.Main.TIMEOUT;



public class ConfigController {

    @FXML
    private TextField tempoResposta;

    public void changeScreen() {
        TIMEOUT = Integer.parseInt(tempoResposta.getText());
        Main.changeScreen("Sample");
        new Simulador().start();
    }
}
