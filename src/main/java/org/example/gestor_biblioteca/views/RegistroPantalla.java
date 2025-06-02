package org.example.gestor_biblioteca.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.gestor_biblioteca.DB.DatabaseConnection;
import org.example.gestor_biblioteca.DAOs.usuariosDao;
import org.example.gestor_biblioteca.models.usuarios;
import org.example.gestor_biblioteca.controllers.Utilidades;

import java.sql.Connection;
import java.sql.SQLException;

public class RegistroPantalla {
    public void mostrar(Stage stage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField();

        Label lblApellido1 = new Label("Primer Apellido:");
        TextField txtApellido1 = new TextField();

        Label lblApellido2 = new Label("Segundo Apellido:");
        TextField txtApellido2 = new TextField();

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();

        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField();

        Label lblPassword = new Label("Contraseña:");
        PasswordField txtPassword = new PasswordField();

        Label lblConfirmPassword = new Label("Confirmar Contraseña:");
        PasswordField txtConfirmPassword = new PasswordField();

        Button btnRegistrar = new Button("Registrar");
        Button btnCancelar = new Button("Cancelar");

        grid.add(lblNombre, 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(lblApellido1, 0, 1);
        grid.add(txtApellido1, 1, 1);
        grid.add(lblApellido2, 0, 2);
        grid.add(txtApellido2, 1, 2);
        grid.add(lblEmail, 0, 3);
        grid.add(txtEmail, 1, 3);
        grid.add(lblTelefono, 0, 4);
        grid.add(txtTelefono, 1, 4);
        grid.add(lblPassword, 0, 5);
        grid.add(txtPassword, 1, 5);
        grid.add(lblConfirmPassword, 0, 6);
        grid.add(txtConfirmPassword, 1, 6);
        grid.add(btnRegistrar, 1, 7);
        grid.add(btnCancelar, 0, 7);

        btnRegistrar.setOnAction(e -> {
            if (validarCampos(txtNombre, txtApellido1, txtEmail, txtPassword, txtConfirmPassword)) {
                registrarUsuario(
                        txtNombre.getText(),
                        txtApellido1.getText(),
                        txtApellido2.getText(),
                        txtEmail.getText(),
                        txtTelefono.getText(),
                        txtPassword.getText()
                );
                stage.close();
            }
        });

        btnCancelar.setOnAction(e -> stage.close());

        Scene scene = new Scene(grid, 500, 450);
        stage.setScene(scene);
        stage.setTitle("Registro de Usuario");
        stage.show();
    }

    private boolean validarCampos(TextField nombre, TextField apellido1, TextField email,
                                  PasswordField password, PasswordField confirmPassword) {
        if (nombre.getText().isEmpty() || apellido1.getText().isEmpty() ||
                email.getText().isEmpty() || password.getText().isEmpty()) {
            Utilidades.mostrarAlerta("Error", "Los campos marcados con * son obligatorios", Alert.AlertType.ERROR);
            return false;
        }

        if (!password.getText().equals(confirmPassword.getText())) {
            Utilidades.mostrarAlerta("Error", "Las contraseñas no coinciden", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void registrarUsuario(String nombre, String apellido1, String apellido2,
                                  String email, String telefono, String password) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            usuariosDao dao = new usuariosDao(conn);
            usuarios nuevoUsuario = new usuarios();

            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setPrimer_apellido(apellido1);
            nuevoUsuario.setSegundo_apellido(apellido2);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setTelefono(telefono);
            nuevoUsuario.setRol(1); // Rol normal por defecto
            nuevoUsuario.setContrasena(Utilidades.encriptarSHA1(password));

            if (dao.save(nuevoUsuario)) {
                Utilidades.mostrarAlerta("Éxito", "Usuario registrado correctamente", Alert.AlertType.INFORMATION);
            } else {
                Utilidades.mostrarAlerta("Error", "No se pudo registrar el usuario", Alert.AlertType.ERROR);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlerta("Error", "Error al conectar con la base de datos", Alert.AlertType.ERROR);
        }
    }
}