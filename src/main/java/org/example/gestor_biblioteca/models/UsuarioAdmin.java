package org.example.gestor_biblioteca.models;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.gestor_biblioteca.views.AdminLibrosPantalla;

public class UsuarioAdmin implements Usuario {
    @Override
    public void mostrarMenuPrincipal(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Button btnLibros = new Button("📚 Libros");
        Button btnPrestamos = new Button("📄 Préstamos");
        Button btnUsuarios = new Button("👤 Usuarios");
        Button btnAdminLibros = new Button("➕ Nuevo Libro");
        Button btnSalir = new Button("Salir");

        btnLibros.setOnAction(e -> mostrarLibros());
        btnPrestamos.setOnAction(e -> mostrarPrestamos());
        btnUsuarios.setOnAction(e -> mostrarUsuarios());
        btnAdminLibros.setOnAction(e -> mostrarAdminLibros(stage));
        btnSalir.setOnAction(e -> stage.close());

        layout.getChildren().addAll(btnLibros, btnPrestamos, btnUsuarios, btnAdminLibros, btnSalir);

        Scene scene = new Scene(layout, 300, 300);
        stage.setScene(scene);
        stage.setTitle("Biblioteca - Administrador");
    }

    private void mostrarAdminLibros(Stage stage) {
        AdminLibrosPantalla adminLibros = new AdminLibrosPantalla();
        adminLibros.mostrar(new Stage());
    }

    private void mostrarLibros() {
        System.out.println("Mostrando libros para administrador");
    }

    private void mostrarPrestamos() {
        System.out.println("Mostrando préstamos para administrador");
    }

    private void mostrarUsuarios() {
        System.out.println("Mostrando gestión de usuarios");
    }
}