package org.example.gestor_biblioteca.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.gestor_biblioteca.DB.DatabaseConnection;
import org.example.gestor_biblioteca.DAOs.prestamosDao;
import org.example.gestor_biblioteca.models.prestamos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class PantallaPrestamosAdmin {
    private Stage stage;
    private TableView<prestamos> tablaPrestamos;

    public PantallaPrestamosAdmin(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        stage.setTitle("Gestión de Préstamos");

        // Crear tabla
        tablaPrestamos = new TableView<>();

        TableColumn<prestamos, Integer> colIdUsuario = new TableColumn<>("ID Usuario");
        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("id_usuario"));

        TableColumn<prestamos, Integer> colIdLibro = new TableColumn<>("ID Libro");
        colIdLibro.setCellValueFactory(new PropertyValueFactory<>("id_libro"));

        TableColumn<prestamos, Date> colFechaPrestamo = new TableColumn<>("Fecha Préstamo");
        colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<>("fecha_prestamo"));

        TableColumn<prestamos, Date> colFechaLimite = new TableColumn<>("Fecha Límite");
        colFechaLimite.setCellValueFactory(new PropertyValueFactory<>("fecha_limite_prestamo"));

        TableColumn<prestamos, Date> colFechaDevolucion = new TableColumn<>("Fecha Devolución");
        colFechaDevolucion.setCellValueFactory(new PropertyValueFactory<>("fecha_devolucion"));

        tablaPrestamos.getColumns().addAll(colIdUsuario, colIdLibro, colFechaPrestamo, colFechaLimite, colFechaDevolucion);

        // Botón para registrar devolución
        Button btnRegistrarDevolucion = new Button("Registrar Devolución");
        btnRegistrarDevolucion.setOnAction(e -> registrarDevolucion());

        VBox layout = new VBox(10, tablaPrestamos, btnRegistrarDevolucion);
        layout.setPadding(new Insets(10));

        cargarTodosPrestamos();

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void cargarTodosPrestamos() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            prestamosDao dao = new prestamosDao(conn);
            ObservableList<prestamos> prestamos = FXCollections.observableArrayList(dao.findAllPrestamos());
            tablaPrestamos.setItems(prestamos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void registrarDevolucion() {
        prestamos prestamoSeleccionado = tablaPrestamos.getSelectionModel().getSelectedItem();
        if (prestamoSeleccionado == null) {
            mostrarAlerta("Error", "Seleccione un préstamo");
            return;
        }

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            prestamosDao dao = new prestamosDao(conn);
            if (dao.registrarDevolucion(prestamoSeleccionado.getId_prestamo())) {
                cargarTodosPrestamos();
            }
        } catch (SQLException e) {
            e.printStackTrace();
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