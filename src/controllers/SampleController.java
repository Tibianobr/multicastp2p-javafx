package controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import sample.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static sample.Main.WAITING;


public class SampleController{

    @FXML
    private Label textoCliente1, textoCliente2, textoCliente3, textoCliente4,
            textoCliente5, textoCliente6, textoCliente7, textoCliente8;

    @FXML
    public Circle imgCliente1, imgCliente2, imgCliente3, imgCliente4,
            imgCliente5, imgCliente6, imgCliente7, imgCliente8;

    @FXML
    private TextField nomeCliente, tempoUso;

    @FXML
    private ChoiceBox<String> selecionarCliente, selecionaClienteDelete;

    @FXML
    private TextArea log;

    private int count=3;

    public void inicia(){
        textoCliente1.setText("Cliente A");
        imgCliente1.setVisible(true);
        imgCliente1.setFill(Color.RED);

        textoCliente2.setText("Cliente B");
        imgCliente2.setVisible(true);
        imgCliente2.setFill(Color.RED);

        textoCliente3.setText("Cliente C");
        imgCliente3.setVisible(true);
        imgCliente3.setFill(Color.RED);

        selecionarCliente.setItems(FXCollections.observableArrayList(textoCliente1.getText(), textoCliente2.getText(), textoCliente3.getText()));
        selecionaClienteDelete.setItems(FXCollections.observableArrayList(textoCliente1.getText(), textoCliente2.getText(), textoCliente3.getText()));

        new Simulador().start();

    }
    public void adicionarCliente() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, InterruptedException {
        if(count == 3) {
            textoCliente4.setText(nomeCliente.getText());
            imgCliente4.setVisible(true);
            imgCliente4.setFill(Color.RED);
            count++;
        }
        else if(count == 4) {
            textoCliente5.setText(nomeCliente.getText());
            imgCliente5.setVisible(true);
            imgCliente5.setFill(Color.RED);
            count++;
        }
        else if(count == 5) {
            textoCliente6.setText(nomeCliente.getText());
            imgCliente6.setVisible(true);
            imgCliente6.setFill(Color.RED);
            count++;
        }
        else if(count == 6) {
            textoCliente7.setText(nomeCliente.getText());
            imgCliente7.setVisible(true);
            imgCliente7.setFill(Color.RED);
            count++;
        }
        else if(count == 7) {
            textoCliente8.setText(nomeCliente.getText());
            imgCliente8.setVisible(true);
            imgCliente8.setFill(Color.RED);
            count++;
        }
        else if(count == 8){
            JOptionPane.showMessageDialog(null, "Numero maximo de clientes atingido");
        }

        selecionarCliente.setItems(FXCollections.observableArrayList(textoCliente1.getText(), textoCliente2.getText(), textoCliente3.getText(), textoCliente4.getText(), textoCliente5.getText(),
                    textoCliente6.getText(), textoCliente7.getText(), textoCliente8.getText()));
        selecionaClienteDelete.setItems(FXCollections.observableArrayList(textoCliente1.getText(), textoCliente2.getText(), textoCliente3.getText(), textoCliente4.getText(), textoCliente5.getText(),
                    textoCliente6.getText(), textoCliente7.getText(), textoCliente8.getText()));

        nomeCliente.clear();
    }


    public void solicitarRecurso() throws InterruptedException {
        String selecionado = selecionarCliente.getValue();

        if(selecionado == textoCliente1.getText()) {
            imgCliente1.setFill(Color.ORANGE);
            log.setText(textoCliente1.getText() + " Solicitou um recurso"+ "\n" + log.getText());
        }

        else if(selecionado == textoCliente2.getText()){
            imgCliente2.setFill(Color.ORANGE);
            log.setText(textoCliente2.getText() + " Solicitou um recurso"+ "\n" + log.getText());
        }

        else if(selecionado == textoCliente3.getText()){
            imgCliente3.setFill(Color.ORANGE);
            log.setText(textoCliente3.getText() + " Solicitou um recurso"+ "\n" + log.getText());
        }

        else if(selecionado == textoCliente4.getText()) {
            imgCliente4.setFill(Color.ORANGE);
            TimeUnit.SECONDS.sleep(1);

            log.setText(textoCliente4.getText() + " Solicitou um recurso"+ "\n" + log.getText());
        }
        else if(selecionado == textoCliente5.getText()) {
            imgCliente5.setFill(Color.ORANGE);
            log.setText(textoCliente5.getText() + " Solicitou um recurso"+ "\n" + log.getText());
        }
        else if(selecionado == textoCliente6.getText()){
            imgCliente6.setFill(Color.ORANGE);
            log.setText(textoCliente6.getText() + " Solicitou um recurso"+ "\n" + log.getText());
        }

        else if(selecionado == textoCliente7.getText()){
            imgCliente7.setFill(Color.ORANGE);
            log.setText(textoCliente7.getText() + " Solicitou um recurso"+ "\n" + log.getText());
        }

        else if(selecionado == textoCliente8.getText()){
            imgCliente8.setFill(Color.ORANGE);
            log.setText(textoCliente8.getText() + " Solicitou um recurso"+ "\n" + log.getText());
        }
    }
}
