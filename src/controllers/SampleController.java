package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;


public class SampleController {

    @FXML
    private Label textoCliente1, textoCliente2, textoCliente3, textoCliente4,
            textoCliente5, textoCliente6, textoCliente7, textoCliente8;

    @FXML
    private Circle imgCliente1, imgCliente2, imgCliente3, imgCliente4,
            imgCliente5, imgCliente6, imgCliente7, imgCliente8;

    @FXML
    private TextField nomeCliente;

    @FXML
    private ChoiceBox<String> selecionarCliente, selecionaClienteDelete;

    public void adicionarCliente(){
        textoCliente1.setText(nomeCliente.getText());
        imgCliente1.setVisible(true);
        selecionarCliente.setItems(FXCollections.observableArrayList(nomeCliente.getText()));
        selecionaClienteDelete.setItems(FXCollections.observableArrayList(nomeCliente.getText()));
        nomeCliente.clear();

        textoCliente2.setText(nomeCliente.getText());
        imgCliente2.setVisible(true);

        textoCliente3.setText(nomeCliente.getText());
        imgCliente3.setVisible(true);

        textoCliente4.setText(nomeCliente.getText());
        imgCliente4.setVisible(true);

        textoCliente5.setText(nomeCliente.getText());
        imgCliente5.setVisible(true);

        textoCliente6.setText(nomeCliente.getText());
        imgCliente6.setVisible(true);

        textoCliente7.setText(nomeCliente.getText());
        imgCliente7.setVisible(true);

        textoCliente8.setText(nomeCliente.getText());
        imgCliente8.setVisible(true);
    }
}
