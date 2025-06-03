package org.example.gestor_biblioteca.views;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.gestor_biblioteca.DB.DatabaseConnection;
import org.example.gestor_biblioteca.DAOs.usuariosDao;
import org.example.gestor_biblioteca.models.Usuario;
import org.example.gestor_biblioteca.models.UsuarioFactory;
import org.example.gestor_biblioteca.models.usuarios;
import org.example.gestor_biblioteca.controllers.Utilidades;

import java.sql.Connection;
import java.sql.SQLException;

public class LoginAcciones {
    public void configurarEventos(LoginPantalla loginPantalla) {
        Button boton = loginPantalla.getBotonLogin();
        Button btnRegistro = loginPantalla.getBotonRegistro();

        boton.setOnAction(event -> {
            TextField campoUsuario = loginPantalla.getCampoUsuario();
            PasswordField campoPassword = loginPantalla.getCampoPassword();

            String usuarioText = campoUsuario.getText().trim();
            String contrasena = campoPassword.getText().trim();

            if (usuarioText.isEmpty() || contrasena.isEmpty()) {
                Utilidades.mostrarAlerta("Campos vacíos", "Por favor completa todos los campos.", Alert.AlertType.WARNING);
                return;
            }

            String hash = Utilidades.encriptarSHA1(contrasena);
            usuarios usuario = autenticarUsuario(usuarioText, hash);

            if (usuario != null) {
                // Obtener el Stage actual
                Stage currentStage = (Stage) boton.getScene().getWindow();

                // Cerrar la ventana actual en el hilo de JavaFX
                Platform.runLater(() -> {
                    currentStage.close();

                    // Crear y mostrar la nueva ventana
                    Stage mainStage = new Stage();
                    Usuario usuarioSistema = UsuarioFactory.crearUsuario(usuario);
                    usuarioSistema.mostrarMenuPrincipal(mainStage);
                    mainStage.show();
                });
            } else {
                Utilidades.mostrarAlerta("Error", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
            }
        });

        btnRegistro.setOnAction(e -> {
            RegistroPantalla registro = new RegistroPantalla();
            Stage registroStage = new Stage();
            registro.mostrar(registroStage);
        });
    }

    private usuarios autenticarUsuario(String usuario, String passwordHash) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            usuariosDao dao = new usuariosDao(conn);
            return dao.findByEmailOrUsuario(usuario, passwordHash);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}