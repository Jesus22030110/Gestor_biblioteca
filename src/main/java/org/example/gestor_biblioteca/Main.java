package org.example.gestor_biblioteca;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.gestor_biblioteca.views.LoginPantalla;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LoginPantalla login = new LoginPantalla();
        login.mostrarLogin(primaryStage);
    }
}