// PantallaUsuariosAdmin.java
package org.example.gestor_biblioteca.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.example.gestor_biblioteca.DB.DatabaseConnection;
import org.example.gestor_biblioteca.DAOs.usuariosDao;
import org.example.gestor_biblioteca.models.usuarios;

import java.sql.Connection;
import java.sql.SQLException;

public class PantallaUsuariosAdmin {

    private TableView<usuarios> tablaUsuarios;

    public void mostrar(Stage stage) {
        stage.setTitle("Gestión de Usuarios");

        // Configurar tabla
        tablaUsuarios = new TableView<>();
        tablaUsuarios.setEditable(true);

        // Columnas de la tabla
        TableColumn<usuarios, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id_usuario"));

        TableColumn<usuarios, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        colNombre.setOnEditCommit(event -> {
            usuarios usuario = event.getRowValue();
            usuario.setNombre(event.getNewValue());
            actualizarUsuario(usuario);
        });

        TableColumn<usuarios, String> colApellido1 = new TableColumn<>("Primer Apellido");
        colApellido1.setCellValueFactory(new PropertyValueFactory<>("primer_apellido"));

        TableColumn<usuarios, String> colApellido2 = new TableColumn<>("Segundo Apellido");
        colApellido2.setCellValueFactory(new PropertyValueFactory<>("segundo_apellido"));

        TableColumn<usuarios, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setOnEditCommit(event -> {
            usuarios usuario = event.getRowValue();
            usuario.setEmail(event.getNewValue());
            actualizarUsuario(usuario);
        });

        TableColumn<usuarios, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colTelefono.setCellFactory(TextFieldTableCell.forTableColumn());
        colTelefono.setOnEditCommit(event -> {
            usuarios usuario = event.getRowValue();
            usuario.setTelefono(event.getNewValue());
            actualizarUsuario(usuario);
        });

        TableColumn<usuarios, Integer> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        // Agregar columnas a la tabla
        tablaUsuarios.getColumns().addAll(
                colId, colNombre, colApellido1, colApellido2, colEmail, colTelefono, colRol
        );

        // Botón para refrescar datos
        Button btnRefresh = new Button("Actualizar Datos");
        btnRefresh.setOnAction(e -> cargarDatosUsuarios());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(tablaUsuarios, btnRefresh);

        // Cargar datos iniciales
        cargarDatosUsuarios();

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void cargarDatosUsuarios() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            usuariosDao dao = new usuariosDao(conn);
            ObservableList<usuarios> usuarios = FXCollections.observableArrayList(dao.findAll());
            tablaUsuarios.setItems(usuarios);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los usuarios: " + e.getMessage());
        }
    }

    private void actualizarUsuario(usuarios usuario) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            usuariosDao dao = new usuariosDao(conn);
            if (dao.update(usuario)) {
                mostrarAlerta("Éxito", "Usuario actualizado correctamente");
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el usuario");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error de base de datos: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}