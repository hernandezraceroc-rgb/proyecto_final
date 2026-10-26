module com.mycompany.proyecto_final_virtual {
    requires javafx.controls;
    requires javafx.fxml;

    opens vista to javafx.fxml;
    exports vista;
}
