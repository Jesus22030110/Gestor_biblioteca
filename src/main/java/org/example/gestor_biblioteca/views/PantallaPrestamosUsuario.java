package org.example.gestor_biblioteca.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.gestor_biblioteca.DB.DatabaseConnection;
import org.example.gestor_biblioteca.DAOs.prestamosDao;
import org.example.gestor_biblioteca.models.libros;
import org.example.gestor_biblioteca.models.prestamos;
import org.example.gestor_biblioteca.models.usuarios;


import javafx.geometry.Insets;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;
import java.util.Optional;

public class PantallaPrestamosUsuario {
    private Stage stage;
    private usuarios usuario;
    private TableView<prestamos> tablaPrestamos;
    private Label lblContador;

    public PantallaPrestamosUsuario(Stage stage, usuarios usuario) {
        this.stage = stage;
        this.usuario = usuario;
    }

    public void mostrar() {
        stage.setTitle("Mis Préstamos");

        // Crear tabla
        tablaPrestamos = new TableView<>();

        TableColumn<prestamos, Integer> colIdLibro = new TableColumn<>("ID Libro");
        colIdLibro.setCellValueFactory(new PropertyValueFactory<>("id_libro"));

        TableColumn<prestamos, Date> colFechaPrestamo = new TableColumn<>("Fecha Préstamo");
        colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<>("fecha_prestamo"));

        TableColumn<prestamos, Date> colFechaLimite = new TableColumn<>("Fecha Límite");
        colFechaLimite.setCellValueFactory(new PropertyValueFactory<>("fecha_limite_prestamo"));

        tablaPrestamos.getColumns().addAll(colIdLibro, colFechaPrestamo, colFechaLimite);

        // Contador de préstamos
        lblContador = new Label();

        // Botón para nuevo préstamo
        Button btnNuevoPrestamo = new Button("Solicitar Préstamo");
        btnNuevoPrestamo.setOnAction(e -> solicitarPrestamo());

        VBox layout = new VBox(10, lblContador, tablaPrestamos, btnNuevoPrestamo);
        layout.setPadding(new Insets(10,10,10,10));

        cargarPrestamosActivos();

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void cargarPrestamosActivos() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            prestamosDao dao = new prestamosDao(conn);
            ObservableList<prestamos> prestamos = FXCollections.observableArrayList(dao.findPrestamosActivos(usuario.getId_usuario()));
            tablaPrestamos.setItems(prestamos);
            lblContador.setText("Libros prestados: " + prestamos.size() + "/2");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al cargar préstamos: " + e.getMessage());
        }
    }

    private void solicitarPrestamo() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            prestamosDao dao = new prestamosDao(conn);
            int prestamosActivos = dao.findPrestamosActivos(usuario.getId_usuario()).size();

            if (prestamosActivos >= 2) {
                mostrarAlerta("Límite alcanzado", "Ya tienes 2 libros prestados");
                return;
            }

            // Crear diálogo de selección con tabla de libros
            Stage libroStage = new Stage();
            libroStage.setTitle("Seleccionar Libro");
            TableView<libros> tablaLibros = new TableView<>();

            // Columna ID
            TableColumn<libros, Integer> colId = new TableColumn<>("ID");
            colId.setCellValueFactory(new PropertyValueFactory<>("id_libro"));

            // Columna Título
            TableColumn<libros, String> colTitulo = new TableColumn<>("Título");
            colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

            // Columna Autor
            TableColumn<libros, String> colAutor = new TableColumn<>("Autor");
            colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));

            tablaLibros.getColumns().addAll(colId, colTitulo, colAutor);

            // Botón de selección
            Button btnSeleccionar = new Button("Seleccionar");
            btnSeleccionar.setOnAction(e -> {
                libros libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();
                if (libroSeleccionado != null) {
                    crearNuevoPrestamo(libroSeleccionado.getId_libro());
                    libroStage.close();
                }
            });

            VBox layout = new VBox(10, tablaLibros, btnSeleccionar);
            Scene scene = new Scene(layout, 500, 400);
            libroStage.setScene(scene);

            // Cargar libros disponibles
            cargarLibrosDisponibles(tablaLibros);
            libroStage.show();

        } catch (SQLException e) {
            mostrarAlerta("Error", "Error de base de datos: " + e.getMessage());
        }
    }

    private void cargarLibrosDisponibles(TableView<libro> tablaLibros) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            libroDao dao = new libroDao(conn);
            ObservableList<libro> libros = FXCollections.observableArrayList(dao.findLibrosDisponibles());
            tablaLibros.setItems(libros);
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al cargar libros: " + e.getMessage());
        }
    }

    private void crearNuevoPrestamo(int idLibro) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            prestamosDao dao = new prestamosDao(conn);
            prestamos nuevoPrestamo = new prestamos();
            nuevoPrestamo.setId_usuario(usuario.getId_usuario());
            nuevoPrestamo.setId_libro(idLibro);

            java.util.Date fechaActual = new java.util.Date();
            LocalDate fechaLimite = fechaActual.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .plusDays(7);

            nuevoPrestamo.setFecha_prestamo(new Date(fechaActual.getTime()));
            nuevoPrestamo.setFecha_limite_prestamo(Date.valueOf(fechaLimite));

            if (dao.save(nuevoPrestamo)) {
                cargarPrestamosActivos();
                mostrarAlerta("Éxito", "Préstamo registrado correctamente");
            } else {
                mostrarAlerta("Error", "No se pudo registrar el préstamo");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al crear préstamo: " + e.getMessage());
        }
    }

    // Método temporal para obtener ID de libro
    private int obtenerIdLibroDesdeUsuario() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Solicitar Préstamo");
        dialog.setHeaderText("Ingrese el ID del libro");
        dialog.setContentText("ID Libro:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                return Integer.parseInt(result.get());
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "ID de libro inválido");
            }
        }
        return -1;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}