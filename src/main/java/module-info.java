module com.mycompany.proyecto_final_virtual {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.proyecto_final_virtual to javafx.fxml;
    exports com.mycompany.proyecto_final_virtual;
}
