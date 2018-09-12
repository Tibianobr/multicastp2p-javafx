package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;


public class SampleController {

    @FXML
    private Label textoCliente4,
            textoCliente5, textoCliente6, textoCliente7, textoCliente8;

    @FXML
    private Circle imgCliente4,
            imgCliente5, imgCliente6, imgCliente7, imgCliente8;

    @FXML
    private TextField nomeCliente;

    @FXML
    private ChoiceBox<String> selecionarCliente, selecionaClienteDelete;

    private int i=4;

    public void adicionarCliente(){
        textoCliente4.setText(nomeCliente.getText());
        imgCliente4.setVisible(true);
        imgCliente4.setFill(Color.RED);

        selecionarCliente.setItems(FXCollections.observableArrayList(nomeCliente.getText()));
        selecionaClienteDelete.setItems(FXCollections.observableArrayList(nomeCliente.getText()));
        nomeCliente.clear();
    }
}
