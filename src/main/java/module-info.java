module org.example.gestor_biblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.gestor_biblioteca.models to javafx.base;
    opens org.example.gestor_biblioteca.views to javafx.fxml;

    exports org.example.gestor_biblioteca;
    exports org.example.gestor_biblioteca.views;
}