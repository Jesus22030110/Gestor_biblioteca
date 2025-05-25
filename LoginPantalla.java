import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

  /*Clase que construye la interfaz gráfica de la ventana de login.
  No contiene lógica, solo diseño visual.*/

public class LoginPantalla {

    // Elementos de la interfaz (accesibles para otras clases como LoginAcciones)
    private TextField campoUsuario;
    private PasswordField campoPassword;
    private Button botonLogin;


     /* Muestra la ventana de login en el Stage proporcionado.
      @param stage Ventana principal*/

    public void mostrarLogin(Stage stage) {
        // Crear componentes
        Label etiquetaUsuario = new Label("Usuario:");
        campoUsuario = new TextField();
        campoUsuario.setPromptText("Ingresa tu usuario");

        Label etiquetaPassword = new Label("Contraseña:");
        campoPassword = new PasswordField();
        campoPassword.setPromptText("Ingresa tu contraseña");

        botonLogin = new Button("Iniciar sesión");

        // Panel de diseño
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Agregar elementos al grid
        grid.add(etiquetaUsuario, 0, 0);
        grid.add(campoUsuario, 1, 0);
        grid.add(etiquetaPassword, 0, 1);
        grid.add(campoPassword, 1, 1);
        grid.add(botonLogin, 1, 2);

        // Escena y ventana
        Scene scene = new Scene(grid, 400, 250);
        stage.setScene(scene);
        stage.setTitle("Sistema Biblioteca - Login");
        stage.show();

        // Lógica de acción se delega a otra clase:
        LoginAcciones loginAcciones = new LoginAcciones();
        loginAcciones.configurarEventos(this);
    }

    // Getters para acceder a los campos desde LoginAcciones
    public TextField getCampoUsuario() {
        return campoUsuario;
    }

    public PasswordField getCampoPassword() {
        return campoPassword;
    }

    public Button getBotonLogin() {
        return botonLogin;
    }
}
