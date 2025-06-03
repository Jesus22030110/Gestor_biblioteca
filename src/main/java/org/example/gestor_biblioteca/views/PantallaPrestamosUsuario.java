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
import org.example.gestor_biblioteca.DAOs.autoresDao;
import org.example.gestor_biblioteca.DAOs.librosDao;
import org.example.gestor_biblioteca.DB.DatabaseConnection;
import org.example.gestor_biblioteca.DAOs.prestamosDao;
import org.example.gestor_biblioteca.models.*;


import javafx.geometry.Insets;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class PantallaPrestamosUsuario {
    private Stage stage;
    private usuarios usuario;
    private TableView<PrestamoCompleto> tablaPrestamos;
    private Label lblContador;

    public PantallaPrestamosUsuario(Stage stage, usuarios usuario) {
        this.stage = stage;
        this.usuario = usuario;
    }

    public void mostrar() {
        stage.setTitle("Mis Préstamos");

        // Crear tabla
        tablaPrestamos = new TableView<>();


        TableColumn<PrestamoCompleto, Integer> colIdLibro = new TableColumn<>("ID Libro");
        colIdLibro.setCellValueFactory(new PropertyValueFactory<>("idLibro"));

        TableColumn<PrestamoCompleto, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<PrestamoCompleto, String> colAutor = new TableColumn<>("Autor");
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));

        TableColumn<PrestamoCompleto, Date> colFechaPrestamo = new TableColumn<>("Fecha Préstamo");
        colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));

        TableColumn<PrestamoCompleto, Date> colFechaLimite = new TableColumn<>("Fecha Límite");
        colFechaLimite.setCellValueFactory(new PropertyValueFactory<>("fechaLimite"));

        TableColumn<PrestamoCompleto, Date> colFechaDevolucion = new TableColumn<>("Fecha Devolución");
        colFechaDevolucion.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));

        tablaPrestamos.getColumns().addAll(colIdLibro, colTitulo, colAutor, colFechaPrestamo, colFechaLimite, colFechaDevolucion);


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
            List<prestamos> prestamosActivos = dao.findPrestamosActivos(usuario.getId_usuario());

            ObservableList<PrestamoCompleto> prestamosCompletos = FXCollections.observableArrayList();
            librosDao libroDao = new librosDao(conn);
            autoresDao autorDao = new autoresDao(conn);

            for (prestamos p : prestamosActivos) {
                libros libro = libroDao.findById(p.getId_libro());
                if (libro != null) {
                    PrestamoCompleto pc = new PrestamoCompleto();
                    pc.setIdPrestamo(p.getId_prestamos());
                    pc.setIdLibro(p.getId_libro());
                    pc.setTitulo(libro.getTitulo_libro());

                    // Obtener autor
                    autores autor = autorDao.findById(libro.getId_autor());
                    if (autor != null) {
                        pc.setAutor(autor.getNombre() + " " + autor.getPrimer_apellido() +
                                (autor.getSegundo_apellido() != null ? " " + autor.getSegundo_apellido() : ""));
                    } else {
                        pc.setAutor("Desconocido");
                    }


                    pc.setFechaPrestamo(p.getFecha_prestamo());
                    pc.setFechaLimite(p.getFecha_limite_prestamo());
                    pc.setFechaDevolucion(p.getFecha_devolucion());

                    prestamosCompletos.add(pc);
                }
            }

            tablaPrestamos.setItems(prestamosCompletos);
            lblContador.setText("Libros prestados: " + prestamosCompletos.size() + "/2");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al cargar préstamos: " + e.getMessage());
        }
    }

    private void solicitarPrestamo() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            prestamosDao dao = new prestamosDao(conn);

            if (dao.findPrestamosActivos(usuario.getId_usuario()).size() >= 2) {
                mostrarAlerta("Límite alcanzado", "Ya tienes 2 libros prestados");
                return;
            }

            // Solicitar ID directamente
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Solicitar Préstamo");
            dialog.setHeaderText("Ingrese el ID del libro");
            dialog.setContentText("ID Libro:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    int idLibro = Integer.parseInt(result.get());
                    crearNuevoPrestamo(idLibro);
                } catch (NumberFormatException e) {
                    mostrarAlerta("Error", "ID inválido: Debe ser un número");
                }
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error de base de datos: " + e.getMessage());
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

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}