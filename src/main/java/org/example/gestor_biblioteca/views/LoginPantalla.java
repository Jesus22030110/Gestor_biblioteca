package org.example.gestor_biblioteca.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginPantalla {
    private TextField campoUsuario;
    private PasswordField campoPassword;
    private Button botonLogin;
    private Button botonRegistro;

    public void mostrarLogin(Stage stage) {
        Label etiquetaUsuario = new Label("Usuario:");
        campoUsuario = new TextField();
        campoUsuario.setPromptText("Ingresa tu usuario");

        Label etiquetaPassword = new Label("Contraseña:");
        campoPassword = new PasswordField();
        campoPassword.setPromptText("Ingresa tu contraseña");

        botonLogin = new Button("Iniciar sesión");
        botonRegistro = new Button("Registrarse");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(etiquetaUsuario, 0, 0);
        grid.add(campoUsuario, 1, 0);
        grid.add(etiquetaPassword, 0, 1);
        grid.add(campoPassword, 1, 1);
        grid.add(botonLogin, 1, 2);
        grid.add(botonRegistro, 1, 3);

        Scene scene = new Scene(grid, 400, 250);
        stage.setScene(scene);
        stage.setTitle("Sistema Biblioteca - Login");
        stage.show();

        LoginAcciones loginAcciones = new LoginAcciones();
        loginAcciones.configurarEventos(this);
    }

    public TextField getCampoUsuario() {
        return campoUsuario;
    }

    public PasswordField getCampoPassword() {
        return campoPassword;
    }

    public Button getBotonLogin() {
        return botonLogin;
    }

    public Button getBotonRegistro() {
        return botonRegistro;
    }
}