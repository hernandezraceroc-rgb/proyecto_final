

module com.mycompany.proyecto_final_virtual {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens vista to javafx.fxml;
    opens modelo to com.google.gson;
    opens usuarios to com.google.gson;

    exports vista;
    exports modelo;
    exports usuarios;
    exports persistencia;
}