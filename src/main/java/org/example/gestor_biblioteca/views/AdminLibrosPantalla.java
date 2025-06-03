package org.example.gestor_biblioteca.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.gestor_biblioteca.DB.DatabaseConnection;
import org.example.gestor_biblioteca.DAOs.*;
import org.example.gestor_biblioteca.controllers.Utilidades;
import org.example.gestor_biblioteca.models.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class AdminLibrosPantalla {
    private Stage stage;
    private ComboBox<String> comboAutores;
    private ComboBox<String> comboEditoriales;
    private ComboBox<String> comboCategorias;
    private TextField txtTitulo;
    private TextField txtAnio;
    private ObservableList<autores> listaAutores;
    private ObservableList<editoriales> listaEditoriales;
    private ObservableList<categorias> listaCategorias;

    public void mostrar(Stage stage) {
        this.stage = stage;
        stage.setTitle("Administración de Libros");

        // Crear controles
        txtTitulo = new TextField();
        txtAnio = new TextField();
        comboAutores = new ComboBox<>();
        comboEditoriales = new ComboBox<>();
        comboCategorias = new ComboBox<>();

        // Configurar ComboBoxes
        configurarCombos();

        // Botones
        Button btnGuardar = new Button("Guardar Libro");
        Button btnCancelar = new Button("Cancelar");

        // Configurar eventos
        btnGuardar.setOnAction(e -> guardarLibro());
        btnCancelar.setOnAction(e -> stage.close());

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Título:"), 0, 0);
        grid.add(txtTitulo, 1, 0);
        grid.add(new Label("Año Publicación:"), 0, 1);
        grid.add(txtAnio, 1, 1);
        grid.add(new Label("Autor:"), 0, 2);
        grid.add(comboAutores, 1, 2);
        grid.add(new Label("Editorial:"), 0, 3);
        grid.add(comboEditoriales, 1, 3);
        grid.add(new Label("Categoría:"), 0, 4);
        grid.add(comboCategorias, 1, 4);

        HBox botonesBox = new HBox(10, btnGuardar, btnCancelar);
        botonesBox.setPadding(new Insets(20, 0, 0, 0));

        VBox layout = new VBox(10, grid, botonesBox);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 400, 350);
        stage.setScene(scene);
        stage.show();
    }

    private void configurarCombos() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Cargar autores
            autoresDao autorDao = new autoresDao(conn);
            listaAutores = FXCollections.observableArrayList(autorDao.findAll());
            comboAutores.setItems(FXCollections.observableArrayList(
                    listaAutores.stream()
                            .map(a -> a.getNombre() + " " + a.getPrimer_apellido() +
                                    (a.getSegundo_apellido() != null ? " " + a.getSegundo_apellido() : ""))
                            .collect(Collectors.toList())
            ));

            // Cargar editoriales
            editorialesDao editorialDao = new editorialesDao(conn);
            listaEditoriales = FXCollections.observableArrayList(editorialDao.findAll());
            comboEditoriales.setItems(FXCollections.observableArrayList(
                    listaEditoriales.stream()
                            .map(editoriales::getEditorial)
                            .collect(Collectors.toList())
            ));

            // Cargar categorías
            categoriasDao categoriaDao = new categoriasDao(conn);
            listaCategorias = FXCollections.observableArrayList(categoriaDao.findAll());
            comboCategorias.setItems(FXCollections.observableArrayList(
                    listaCategorias.stream()
                            .map(categorias::getCategoria)
                            .collect(Collectors.toList())
            ));

            // Seleccionar primeros elementos si existen
            if (!listaAutores.isEmpty()) comboAutores.getSelectionModel().selectFirst();
            if (!listaEditoriales.isEmpty()) comboEditoriales.getSelectionModel().selectFirst();
            if (!listaCategorias.isEmpty()) comboCategorias.getSelectionModel().selectFirst();

        } catch (SQLException e) {
            Utilidades.mostrarAlerta("Error", "Error al cargar datos: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void guardarLibro() {
        try {
            // Validar campos
            if (txtTitulo.getText().isEmpty() || txtAnio.getText().isEmpty() ||
                    comboAutores.getSelectionModel().isEmpty() ||
                    comboEditoriales.getSelectionModel().isEmpty() ||
                    comboCategorias.getSelectionModel().isEmpty()) {
                Utilidades.mostrarAlerta("Error", "Todos los campos son obligatorios", Alert.AlertType.ERROR);
                return;
            }

            String titulo = txtTitulo.getText();
            int anio = Integer.parseInt(txtAnio.getText());

            // Obtener IDs seleccionados
            int autorIndex = comboAutores.getSelectionModel().getSelectedIndex();
            int editorialIndex = comboEditoriales.getSelectionModel().getSelectedIndex();
            int categoriaIndex = comboCategorias.getSelectionModel().getSelectedIndex();

            if (autorIndex < 0 || editorialIndex < 0 || categoriaIndex < 0) {
                Utilidades.mostrarAlerta("Error", "Selección inválida en los combos", Alert.AlertType.ERROR);
                return;
            }

            int idAutor = listaAutores.get(autorIndex).getId_autor();
            int idEditorial = listaEditoriales.get(editorialIndex).getId_editorial();
            int idCategoria = listaCategorias.get(categoriaIndex).getId_categoria();

            System.out.println("Guardando libro con parámetros:");
            System.out.println("Título: " + titulo);
            System.out.println("Año: " + anio);
            System.out.println("ID Autor: " + idAutor);
            System.out.println("ID Editorial: " + idEditorial);
            System.out.println("ID Categoría: " + idCategoria);

            // Guardar en base de datos
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
                    limpiarCampos();
                } else {
                    Utilidades.mostrarAlerta("Error", "No se pudo guardar el libro", Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                System.err.println("Error de base de datos: " + e.getMessage());
                e.printStackTrace();
                Utilidades.mostrarAlerta("Error", "Error de base de datos: " + e.getMessage(), Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            Utilidades.mostrarAlerta("Error", "El año debe ser un número válido", Alert.AlertType.ERROR);
            System.err.println("Error de formato numérico: " + e.getMessage());
        } catch (Exception e) {
            Utilidades.mostrarAlerta("Error", "Error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtTitulo.clear();
        txtAnio.clear();
        if (!listaAutores.isEmpty()) comboAutores.getSelectionModel().selectFirst();
        if (!listaEditoriales.isEmpty()) comboEditoriales.getSelectionModel().selectFirst();
        if (!listaCategorias.isEmpty()) comboCategorias.getSelectionModel().selectFirst();
    }
}