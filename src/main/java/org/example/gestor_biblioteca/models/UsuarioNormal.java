package org.example.gestor_biblioteca.models;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UsuarioNormal implements Usuario {
    @Override
    public void mostrarMenuPrincipal(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Button btnLibros = new Button("📚 Libros");
        Button btnPrestamos = new Button("📄 Préstamos");
        Button btnSalir = new Button("Salir");

        btnLibros.setOnAction(e -> mostrarLibros());
        btnPrestamos.setOnAction(e -> mostrarPrestamos());
        btnSalir.setOnAction(e -> stage.close());

        layout.getChildren().addAll(btnLibros, btnPrestamos, btnSalir);

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Biblioteca - Usuario Normal");
    }

    private void mostrarLibros() {
        // Implementar lógica para mostrar libros
        System.out.println("Mostrando libros para usuario normal");
    }

    private void mostrarPrestamos() {
        // Implementar lógica para mostrar préstamos
        System.out.println("Mostrando préstamos para usuario normal");
    }
}