package sample;

import controllers.SampleController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

/*
    Não iremos comentar parte gráfica nesse trabalho
 */

public class Main extends Application {
    // Variáveis que iremos utilizar mais para frente no trabalho e precisamos delas 'fixas'
    public final static int PORT = 6789;
    public final static String ADDRESS = "239.0.0.0";
    public final static Integer WAITING = 1;
    public final static Integer BUSY = 2;
    public static Integer TIMEOUT;
    public static SampleController SAMPLECONTROLLER;

    private static Stage stage;

    private static Scene Config;
    private static Scene Sample;

    @Override
    public void start(Stage primaryStage) throws Exception {

        stage = primaryStage;

        primaryStage.setTitle("Configuração SD01");

        Parent fxmlLogin = FXMLLoader.load(getClass().getResource("/resources/config.fxml"));
        Config = new Scene(fxmlLogin);

        Parent fxmlMenu = FXMLLoader.load(getClass().getResource("/resources/sample.fxml"));
        Sample = new Scene(fxmlMenu);

        primaryStage.setTitle("Sistemas Recursos Compartilhados");
        primaryStage.setScene(Config);
        primaryStage.show();
    }

    public static void changeScreen(String scr) {
        switch (scr) {
            case "Config":
                stage.setScene(Config);
                break;
            case "Sample":
                stage.setScene(Sample);
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
