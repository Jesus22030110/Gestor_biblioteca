import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginAcciones {

    // Interfaz estrategia
    interface AutenticacionStrategy {
        boolean autenticar(String usuario, String passwordHash);
    }

    // Estrategia preparada para BD
    class AutenticacionBD implements AutenticacionStrategy {
        @Override
        public boolean autenticar(String usuario, String passwordHash) {
            // Aquí va la lógica para consultar la base de datos
            // Por ejemplo: hacer consulta SQL para verificar usuario y password
            // Por ahora devolvemos false para simular sin conexión
            return false;
        }
    }

    private final AutenticacionStrategy estrategia;

    // Constructor usa estrategia preparada para BD (sin listas)
    public LoginAcciones() {
        this.estrategia = new AutenticacionBD();
    }

    public void configurarEventos(LoginPantalla loginPantalla) {
        Button boton = loginPantalla.getBotonLogin();
        boton.setOnAction(event -> {
            TextField campoUsuario = loginPantalla.getCampoUsuario();
            PasswordField campoPassword = loginPantalla.getCampoPassword();

            String usuario = campoUsuario.getText().trim();
            String contrasena = campoPassword.getText().trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                mostrarAlerta("Campos vacíos", "Por favor completa todos los campos.");
                return;
            }

            String hash = encriptarSHA1(contrasena);

            boolean autenticado = estrategia.autenticar(usuario, hash);

            if (autenticado) {
                mostrarAlerta("Bienvenido", "Inicio de sesión exitoso.");
                VistaPrincipal.mostrar();
                ((Stage) boton.getScene().getWindow()).close();
            } else {
                mostrarAlerta("Error", "Usuario o contraseña incorrectos.");
            }
        });
    }

    private String encriptarSHA1(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] resultado = md.digest(texto.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : resultado) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
