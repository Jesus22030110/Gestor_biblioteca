package org.example.gestor_biblioteca.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.gestor_biblioteca.DB.DatabaseConnection;
import org.example.gestor_biblioteca.DAOs.librosDao;
import org.example.gestor_biblioteca.models.libros;
import org.example.gestor_biblioteca.controllers.Utilidades;

import java.sql.Connection;
import java.sql.SQLException;

public class AdminLibrosPantalla {
    public void mostrar(Stage stage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label lblTitulo = new Label("Título:");
        TextField txtTitulo = new TextField();

        Label lblAnio = new Label("Año Publicación:");
        TextField txtAnio = new TextField();

        Label lblEditorial = new Label("ID Editorial:");
        TextField txtEditorial = new TextField();

        Label lblAutor = new Label("ID Autor:");
        TextField txtAutor = new TextField();

        Label lblCategoria = new Label("ID Categoría:");
        TextField txtCategoria = new TextField();

        Button btnGuardar = new Button("Guardar Libro");
        Button btnCancelar = new Button("Cancelar");

        grid.add(lblTitulo, 0, 0);
        grid.add(txtTitulo, 1, 0);
        grid.add(lblAnio, 0, 1);
        grid.add(txtAnio, 1, 1);
        grid.add(lblEditorial, 0, 2);
        grid.add(txtEditorial, 1, 2);
        grid.add(lblAutor, 0, 3);
        grid.add(txtAutor, 1, 3);
        grid.add(lblCategoria, 0, 4);
        grid.add(txtCategoria, 1, 4);
        grid.add(btnGuardar, 1, 5);
        grid.add(btnCancelar, 0, 5);

        btnGuardar.setOnAction(e -> {
            try {
                guardarLibro(
                        txtTitulo.getText(),
                        Integer.parseInt(txtAnio.getText()),
                        Integer.parseInt(txtEditorial.getText()),
                        Integer.parseInt(txtAutor.getText()),
                        Integer.parseInt(txtCategoria.getText())
                );
                stage.close();
            } catch (NumberFormatException ex) {
                Utilidades.mostrarAlerta("Error", "Los campos numéricos deben contener valores válidos", Alert.AlertType.ERROR);
            }
        });

        btnCancelar.setOnAction(e -> stage.close());

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Administración de Libros");
        stage.show();
    }

    private void guardarLibro(String titulo, int anio, int idEditorial, int idAutor, int idCategoria) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            librosDao dao = new librosDao(conn);
            libros nuevoLibro = new libros();

            nuevoLibro.setTitulo_libro(titulo);
            nuevoLibro.setAno_publicacion(anio);
            nuevoLibro.setId_editorial(idEditorial);
            nuevoLibro.setId_autor(idAutor);
            nuevoLibro.setId_categoria(idCategoria);

            if (dao.save(nuevoLibro)) {
                Utilidades.mostrarAlerta("Éxito", "Libro guardado correctamente", Alert.AlertType.INFORMATION);
            } else {
                Utilidades.mostrarAlerta("Error", "No se pudo guardar el libro", Alert.AlertType.ERROR);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlerta("Error", "Error al conectar con la base de datos", Alert.AlertType.ERROR);
        }
    }
}