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

import static sample.Main.SAMPLECONTROLLER;
import static sample.Main.WAITING;


public class SampleController{
    InetAddress group;
    Manager manager;
    Integer log_linhas = 0;
    Client client,client1,client2,client3,client4,client5,client6,client7;

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
        SAMPLECONTROLLER = this;
        Server servidor = new Server();
        group = servidor.criarGrupo();
        Recurso processador = new Recurso("Recurso 001", WAITING);
        Recurso memoria = new Recurso("Recurso 002", WAITING);
        List<Recurso> recursos = Arrays.asList(processador, memoria);
        manager = new Manager(recursos);
        servidor.configurar(group, "Server 01", manager);

        final CyclicBarrier initial_gate = new CyclicBarrier(3);
        client = new Client(group, "Cliente A", initial_gate, manager);
        client1 = new Client(group, "Cliente B", initial_gate, manager);
        client2 = new Client(group, "Cliente C", initial_gate, manager);

        client1.start();
        client.start();
        client2.start();

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



    }
    public void adicionarCliente() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, InterruptedException {
        if(count == 3) {
            textoCliente4.setText(nomeCliente.getText());
            imgCliente4.setVisible(true);
            imgCliente4.setFill(Color.RED);
            count++;
            client3 = new Client(group, nomeCliente.getText(), null, manager);
            client3.start();
        }
        else if(count == 4) {
            textoCliente5.setText(nomeCliente.getText());
            imgCliente5.setVisible(true);
            imgCliente5.setFill(Color.RED);
            count++;
            client4 = new Client(group, nomeCliente.getText(), null, manager);
            client4.start();
        }
        else if(count == 5) {
            textoCliente6.setText(nomeCliente.getText());
            imgCliente6.setVisible(true);
            imgCliente6.setFill(Color.RED);
            count++;
            client5 = new Client(group, nomeCliente.getText(), null, manager);
            client5.start();
        }
        else if(count == 6) {
            textoCliente7.setText(nomeCliente.getText());
            imgCliente7.setVisible(true);
            imgCliente7.setFill(Color.RED);
            count++;
            Client client6 = new Client(group, nomeCliente.getText(), null, manager);
            client6.start();
        }
        else if(count == 7) {
            textoCliente8.setText(nomeCliente.getText());
            imgCliente8.setVisible(true);
            imgCliente8.setFill(Color.RED);
            count++;
            client7 = new Client(group, nomeCliente.getText(), null, manager);
            client7.start();
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


    public void solicitarRecurso() throws InterruptedException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException {
        String selecionado = selecionarCliente.getValue();

        if(selecionado == textoCliente1.getText()) {
            imgCliente1.setFill(Color.ORANGE);
         //   log.setText(textoCliente1.getText() + " Solicitou um recurso"+ "\n" + log.getText());
            client.request_time = Integer.parseInt(tempoUso.getText())* 1000;
            client.enviar("-1", "request");
        }

        else if(selecionado == textoCliente2.getText()){
            imgCliente2.setFill(Color.ORANGE);
          //  log.setText(textoCliente2.getText() + " Solicitou um recurso"+ "\n" + log.getText());
            client1.request_time = Integer.parseInt(tempoUso.getText())* 1000;
            client1.enviar("-1", "request");
        }

        else if(selecionado == textoCliente3.getText()){
            imgCliente3.setFill(Color.ORANGE);
          //  log.setText(textoCliente3.getText() + " Solicitou um recurso"+ "\n" + log.getText());
            client2.request_time = Integer.parseInt(tempoUso.getText())* 1000;
            client2.enviar("-1", "request");
        }

        else if(selecionado == textoCliente4.getText()) {
            imgCliente4.setFill(Color.ORANGE);
            TimeUnit.SECONDS.sleep(1);
            System.out.println(client3);
            client3.request_time = Integer.parseInt(tempoUso.getText())* 1000;
           // log.setText(textoCliente4.getText() + " Solicitou um recurso"+ "\n" + log.getText());
            client3.enviar("-1", "request");
        }
        else if(selecionado == textoCliente5.getText()) {
            imgCliente5.setFill(Color.ORANGE);
            client4.request_time = Integer.parseInt(tempoUso.getText())* 1000;
            //  log.setText(textoCliente5.getText() + " Solicitou um recurso"+ "\n" + log.getText());
            client4.enviar("-1", "request");
        }
        else if(selecionado == textoCliente6.getText()){
            imgCliente6.setFill(Color.ORANGE);
            client5.request_time = Integer.parseInt(tempoUso.getText())* 1000;
            //   log.setText(textoCliente6.getText() + " Solicitou um recurso"+ "\n" + log.getText());
            client5.enviar("-1", "request");
        }

        else if(selecionado == textoCliente7.getText()){
            imgCliente7.setFill(Color.ORANGE);
            client6.request_time = Integer.parseInt(tempoUso.getText()) * 1000;
            //    log.setText(textoCliente7.getText() + " Solicitou um recurso"+ "\n" + log.getText());
            client6.enviar("-1", "request");
        }

        else if(selecionado == textoCliente8.getText()){
            imgCliente8.setFill(Color.ORANGE);
            client7.request_time = Integer.parseInt(tempoUso.getText()) * 1000;
            // log.setText(textoCliente8.getText() + " Solicitou um recurso"+ "\n" + log.getText());
            client7.enviar("-1", "request");
        }
    }

    public void removerCliente() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, InterruptedException {
        String opcao = selecionaClienteDelete.getValue();

        if(opcao == textoCliente1.getText())
        {
            textoCliente1.setText(null);
            imgCliente1.setVisible(false);

            client.leaveGroup();
        }
        else if(opcao == textoCliente2.getText())
        {
            textoCliente2.setText(null);
            imgCliente2.setVisible(false);

            client1.leaveGroup();
        }
        else if(opcao == textoCliente3.getText())
        {
            textoCliente3.setText(null);
            imgCliente3.setVisible(false);

            client2.leaveGroup();
        }
        else if(opcao == textoCliente4.getText())
        {
            textoCliente4.setText(null);
            imgCliente4.setVisible(false);

            client3.leaveGroup();

        }
        else if(opcao == textoCliente5.getText())
        {
            textoCliente5.setText(null);
            imgCliente5.setVisible(false);

            client4.leaveGroup();
        }
        else if(opcao == textoCliente6.getText())
        {
            textoCliente6.setText(null);
            imgCliente6.setVisible(false);

            client5.leaveGroup();
        }
        else if(opcao == textoCliente7.getText())
        {
            textoCliente7.setText(null);
            imgCliente7.setVisible(false);

            client6.leaveGroup();
        }
        else if(opcao == textoCliente8.getText())
        {
            textoCliente8.setText(null);
            imgCliente8.setVisible(false);

            client7.leaveGroup();
        }
    }

    public void sairDoRecurso() throws NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        String opcao = selecionaClienteDelete.getValue();

        if(opcao == textoCliente1.getText())
        {
            imgCliente1.setFill(Color.GREEN);
            client.recursos.getUsingRecurso(client).sairDoRecurso();
        }
        else if(opcao == textoCliente2.getText())
        {
            imgCliente2.setFill(Color.GREEN);
            client1.recursos.getUsingRecurso(client1).sairDoRecurso();
        }
        else if(opcao == textoCliente3.getText())
        {
            imgCliente3.setFill(Color.GREEN);
            client2.recursos.getUsingRecurso(client2).sairDoRecurso();
        }
        else if(opcao == textoCliente4.getText())
        {
            imgCliente4.setFill(Color.GREEN);
            client3.recursos.getUsingRecurso(client3).sairDoRecurso();
        }
        else if(opcao == textoCliente5.getText())
        {
            imgCliente5.setFill(Color.GREEN);
            client4.recursos.getUsingRecurso(client4).sairDoRecurso();
        }
        else if(opcao == textoCliente6.getText())
        {
            imgCliente6.setFill(Color.GREEN);
            client5.recursos.getUsingRecurso(client5).sairDoRecurso();

        }
        else if(opcao == textoCliente7.getText())
        {
            imgCliente7.setFill(Color.GREEN);
            client6.recursos.getUsingRecurso(client6).sairDoRecurso();

        }
        else if(opcao == textoCliente8.getText())
        {
            imgCliente8.setFill(Color.GREEN);
            client7.recursos.getUsingRecurso(client7).sairDoRecurso();
        }
    }

    public void atualizarLog(String msg)
    {

    //    log_linhas++;
     //   log.setText(msg);
       // if (log != null && msg != null)
         //   log.appendText("\n" + msg);
    }
}
