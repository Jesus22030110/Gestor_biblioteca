import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


 /* Clase que representa la ventana principal del sistema.
  Se muestra después del login exitoso.*/

public class VistaPrincipal {

    /* Muestra la ventana principal del sistema.*/

    public static void mostrar() {
        Stage ventana = new Stage();
        ventana.setTitle("Sistema de Gestión de Biblioteca Digital");

        Label titulo = new Label("Bienvenido a la Biblioteca Digital");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button btnLibros = new Button("📚 Libros");
        Button btnPrestamos = new Button("📄 Préstamos");
        Button btnUsuarios = new Button("👤 Usuarios");
        Button btnSalir = new Button("Salir");

        btnLibros.setMaxWidth(Double.MAX_VALUE);
        btnPrestamos.setMaxWidth(Double.MAX_VALUE);
        btnUsuarios.setMaxWidth(Double.MAX_VALUE);
        btnSalir.setMaxWidth(Double.MAX_VALUE);

        btnSalir.setOnAction(e -> ventana.close());

        VBox layout = new VBox(15, titulo, btnLibros, btnPrestamos, btnUsuarios, btnSalir);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f2f2f2;");

        Scene escena = new Scene(layout, 300, 250);
        ventana.setScene(escena);
        ventana.show();
    }
}
