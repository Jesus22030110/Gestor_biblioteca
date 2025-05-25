import javafx.application.Application;
import javafx.stage.Stage;
 /* Clase principal del sistema de gestión de biblioteca digital.
   Inicia la aplicación JavaFX y muestra la ventana de login.*/

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Se crea una instancia de la clase que contiene la interfaz del login
        LoginPantalla loginPantalla = new LoginPantalla();
        // Se llama al metodo que lanza la ventana de login
        loginPantalla.mostrarLogin(primaryStage);
    }

    public static void main(String[] args) {
        // Metodo que lanza la aplicación JavaFX
        launch(args);
    }
}

