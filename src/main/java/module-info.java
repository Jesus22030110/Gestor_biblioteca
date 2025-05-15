module org.example.gestor_biblioteca {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.gestor_biblioteca to javafx.fxml;
    exports org.example.gestor_biblioteca;
}