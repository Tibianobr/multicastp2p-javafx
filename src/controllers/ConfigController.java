package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import sample.Main;


public class ConfigController {

    @FXML
    private TextField tempoResposta;

    public void changeScreen() {
        System.out.println("OK");

        Main.changeScreen("Sample");
    }
}