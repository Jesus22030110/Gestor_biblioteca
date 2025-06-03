package org.example.gestor_biblioteca.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.gestor_biblioteca.DB.DatabaseConnection;
import org.example.gestor_biblioteca.DAOs.*;
import org.example.gestor_biblioteca.controllers.Utilidades;
import org.example.gestor_biblioteca.models.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PantallaLibros {
    private Stage stage;
    private boolean isAdmin;
    private TableView<LibroCompleto> tablaLibros;
    private ComboBox<String> comboFiltroCategoria;
    private ComboBox<String> comboFiltroEditorial;
    private ComboBox<Integer> comboFiltroAnio;
    private TextField txtFiltroAutor;
    private ObservableList<LibroCompleto> librosData;

    public PantallaLibros(Stage stage, boolean isAdmin) {
        this.stage = stage;
        this.isAdmin = isAdmin;
        mostrar();
    }

    public void mostrar() {
        stage.setTitle("Catálogo de Libros" + (isAdmin ? " (Admin)" : ""));

        // Crear controles
        tablaLibros = new TableView<>();
        comboFiltroCategoria = new ComboBox<>();
        comboFiltroEditorial = new ComboBox<>();
        comboFiltroAnio = new ComboBox<>();
        txtFiltroAutor = new TextField();
        Button btnFiltrar = new Button("Filtrar");
        Button btnLimpiar = new Button("Limpiar Filtros");


        configurarTabla();

        cargarDatosIniciales();

        configurarFiltros();

        // Botones de acción
        HBox botonesBox = new HBox(10, btnFiltrar, btnLimpiar);
        if (isAdmin) {
            Button btnAgregar = new Button("Agregar Nuevo Libro");
            botonesBox.getChildren().add(btnAgregar);
            btnAgregar.setOnAction(e -> mostrarAdminLibros());
        }

        // Layout
        VBox filtrosBox = new VBox(10,
                new Label("Filtros:"),
                new HBox(10, new Label("Categoría:"), comboFiltroCategoria),
                new HBox(10, new Label("Editorial:"), comboFiltroEditorial),
                new HBox(10, new Label("Año:"), comboFiltroAnio),
                new HBox(10, new Label("Autor:"), txtFiltroAutor),
                botonesBox
        );
        filtrosBox.setPadding(new Insets(10));

        VBox layout = new VBox(10, filtrosBox, tablaLibros);
        layout.setPadding(new Insets(10));

        // Eventos
        btnFiltrar.setOnAction(e -> aplicarFiltros());
        btnLimpiar.setOnAction(e -> limpiarFiltros());

        // Mostrar
        Scene scene = new Scene(layout, 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void configurarTabla() {
        TableColumn<LibroCompleto, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colTitulo.setMinWidth(200);

        TableColumn<LibroCompleto, Integer> colAnio = new TableColumn<>("Año");
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colAnio.setMinWidth(60);

        TableColumn<LibroCompleto, String> colEditorial = new TableColumn<>("Editorial");
        colEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        colEditorial.setMinWidth(120);

        TableColumn<LibroCompleto, String> colAutor = new TableColumn<>("Autor");
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAutor.setMinWidth(150);

        TableColumn<LibroCompleto, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colCategoria.setMinWidth(120);

        tablaLibros.getColumns().addAll(colTitulo, colAnio, colEditorial, colAutor, colCategoria);
    }

    private void cargarDatosIniciales() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Obtener libros con información completa
            librosData = FXCollections.observableArrayList(obtenerLibrosCompletos(conn));

            // Cargar combos
            cargarComboCategorias(conn);
            cargarComboEditoriales(conn);
            cargarComboAnios();

            tablaLibros.setItems(librosData);
        } catch (SQLException e) {
            Utilidades.mostrarAlerta("Error", "Error al cargar datos: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private List<LibroCompleto> obtenerLibrosCompletos(Connection conn) throws SQLException {
        librosDao libroDao = new librosDao(conn);
        autoresDao autorDao = new autoresDao(conn);
        editorialesDao editorialDao = new editorialesDao(conn);
        categoriasDao categoriaDao = new categoriasDao(conn);

        List<libros> librosBase = libroDao.findAll();
        System.out.println("Libros base encontrados: " + librosBase.size());

        List<LibroCompleto> librosCompletos = new ArrayList<>();

        for (libros libro : librosBase) {
            LibroCompleto lc = new LibroCompleto();
            lc.setId(libro.getId_libro());
            lc.setTitulo(libro.getTitulo_libro());
            lc.setAnio(libro.getAno_publicacion());

            try {
                // Editorial
                editoriales editorial = editorialDao.findById(libro.getId_editorial());
                lc.setEditorial(editorial != null ? editorial.getEditorial() : "Desconocida");

                // Autor
                autores autor = autorDao.findById(libro.getId_autor());
                if (autor != null) {
                    lc.setAutor(autor.getNombre() + " " + autor.getPrimer_apellido() +
                            (autor.getSegundo_apellido() != null ? " " + autor.getSegundo_apellido() : ""));
                } else {
                    lc.setAutor("Desconocido");
                    System.err.println("Autor no encontrado para ID: " + libro.getId_autor());
                }

                // Categoría
                categorias categoria = categoriaDao.findById(libro.getId_categoria());
                lc.setCategoria(categoria != null ? categoria.getCategoria() : "Desconocida");

            } catch (Exception e) {
                System.err.println("Error procesando libro ID " + libro.getId_libro() + ": " + e.getMessage());
                e.printStackTrace();
            }

            librosCompletos.add(lc);
        }

        return librosCompletos;
    }

    private void cargarComboCategorias(Connection conn) throws SQLException {
        categoriasDao dao = new categoriasDao(conn);
        ObservableList<String> categorias = FXCollections.observableArrayList(
                dao.findAll().stream()
                        .map(c -> c.getCategoria())  // Corregido aquí
                        .collect(Collectors.toList())
        );
        categorias.add(0, "Todas");
        comboFiltroCategoria.setItems(categorias);
        comboFiltroCategoria.getSelectionModel().selectFirst();
    }

    private void cargarComboEditoriales(Connection conn) throws SQLException {
        editorialesDao dao = new editorialesDao(conn);
        ObservableList<String> editoriales = FXCollections.observableArrayList(
                dao.findAll().stream()
                        .map(e -> e.getEditorial())  // Corregido aquí
                        .collect(Collectors.toList())
        );
        editoriales.add(0, "Todas");
        comboFiltroEditorial.setItems(editoriales);
        comboFiltroEditorial.getSelectionModel().selectFirst();
    }

    private void cargarComboAnios() {
        ObservableList<Integer> anios = FXCollections.observableArrayList(
                librosData.stream()
                        .map(LibroCompleto::getAnio)
                        .distinct()
                        .sorted(Comparator.reverseOrder())
                        .collect(Collectors.toList())
        );
        anios.add(0, -1); // Valor para "Todos"
        comboFiltroAnio.setItems(anios);
        comboFiltroAnio.getSelectionModel().selectFirst();
    }

    private void configurarFiltros() {
        // Filtrado automático al cambiar cualquier filtro
        comboFiltroCategoria.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        comboFiltroEditorial.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        comboFiltroAnio.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        txtFiltroAutor.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
    }

    private void aplicarFiltros() {
        String categoria = comboFiltroCategoria.getValue();
        String editorial = comboFiltroEditorial.getValue();
        Integer anio = comboFiltroAnio.getValue();
        String autor = txtFiltroAutor.getText().toLowerCase();

        FilteredList<LibroCompleto> filteredData = new FilteredList<>(librosData, libro ->
                (categoria == null || categoria.equals("Todas") || libro.getCategoria().equals(categoria)) &&
                        (editorial == null || editorial.equals("Todas") || libro.getEditorial().equals(editorial)) &&
                        (anio == null || anio == -1 || libro.getAnio() == anio) &&
                        (autor.isEmpty() || libro.getAutor().toLowerCase().contains(autor))
        );

        SortedList<LibroCompleto> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaLibros.comparatorProperty());
        tablaLibros.setItems(sortedData);
    }

    private void limpiarFiltros() {
        comboFiltroCategoria.getSelectionModel().selectFirst();
        comboFiltroEditorial.getSelectionModel().selectFirst();
        comboFiltroAnio.getSelectionModel().selectFirst();
        txtFiltroAutor.clear();
    }

    private void mostrarAdminLibros() {
        AdminLibrosPantalla adminLibros = new AdminLibrosPantalla();
        Stage adminStage = new Stage();
        adminLibros.mostrar(adminStage);
        adminStage.setOnHidden(e -> actualizarDatos());
    }

    private void actualizarDatos() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            librosData.setAll(obtenerLibrosCompletos(conn));
            cargarComboAnios();
            aplicarFiltros();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}