package vista;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import usuarios.gestorusuarios;

public class RegistroController {

    @FXML
    private TextField txtNombre;

    @FXML
    private PasswordField txtClave;

    @FXML
    private PasswordField txtConfirmar;

    @FXML
    private Button btnCrear;

    @FXML
    private Hyperlink btnVolver;

    private final gestorusuarios gestor = new gestorusuarios();

    public void initialize() {
        btnCrear.setOnAction(e -> registrar());
        btnVolver.setOnAction(e -> abrir("login"));
    }

    private void registrar() {
        String nombre = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
        String clave = txtClave.getText() == null ? "" : txtClave.getText();
        String confirmar = txtConfirmar.getText() == null ? "" : txtConfirmar.getText();

        if (nombre.isEmpty() || clave.length() < 4) {
            alerta(Alert.AlertType.WARNING,
                    "El usuario no puede estar vacío y la contraseña debe tener al menos 4 caracteres.");
            return;
        }
        if (!clave.equals(confirmar)) {
            alerta(Alert.AlertType.WARNING, "Las contraseñas no coinciden.");
            return;
        }
        if (!gestor.registrarCliente(nombre, clave)) {
            alerta(Alert.AlertType.ERROR, "Ese usuario ya existe.");
            return;
        }

        alerta(Alert.AlertType.INFORMATION, "Cuenta creada. Ya puedes iniciar sesión.");
        abrir("login");
    }

    private void abrir(String fxml) {
        try {
            App.setRoot(fxml);
        } catch (IOException ex) {
            alerta(Alert.AlertType.ERROR, "No se pudo abrir la pantalla: " + ex.getMessage());
        }
    }

    private void alerta(Alert.AlertType tipo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("UniShop");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
