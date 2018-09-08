package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public final static int PORT = 6789;
    public final static String ADDRESS = "239.0.0.0";
    public final static Integer BUSY = 2;
    public final static Integer WAITING = 1;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../resources/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        new Simulador().simular();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
