package vista;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import usuarios.gestorusuarios;
import usuarios.usuario;

public class LoginController {

    private static final String ESTILO_ACTIVO =
            "-fx-background-color: #1A56DB; -fx-background-radius: 20; "
            + "-fx-text-fill: white; -fx-padding: 4 12 4 12;";
    private static final String ESTILO_INACTIVO =
            "-fx-background-color: #EBF5FF; -fx-background-radius: 20; "
            + "-fx-text-fill: #1a56db; -fx-padding: 4 12 4 12;";

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Button btnIngresar;

    @FXML
    private Button btnCliente;

    @FXML
    private Button btnAdmin;

    @FXML
    private Hyperlink btnRegistrar;

    private final gestorusuarios gestor = new gestorusuarios();
    private usuario.Rol rolSeleccionado = usuario.Rol.CLIENTE;

    public void initialize() {
        btnCliente.setOnAction(e -> seleccionarRol(usuario.Rol.CLIENTE));
        btnAdmin.setOnAction(e -> seleccionarRol(usuario.Rol.ADMIN));
        btnIngresar.setOnAction(e -> ingresar());
        txtContrasena.setOnAction(e -> ingresar());
        btnRegistrar.setOnAction(e -> abrir("registro"));
        marcarRol();
    }

    private void seleccionarRol(usuario.Rol rol) {
        rolSeleccionado = rol;
        marcarRol();
    }

    private void marcarRol() {
        boolean cliente = rolSeleccionado == usuario.Rol.CLIENTE;
        btnCliente.setStyle(cliente ? ESTILO_ACTIVO : ESTILO_INACTIVO);
        btnAdmin.setStyle(cliente ? ESTILO_INACTIVO : ESTILO_ACTIVO);
    }

    private void ingresar() {
        String nombre = txtCorreo.getText() == null ? "" : txtCorreo.getText().trim();
        String clave = txtContrasena.getText() == null ? "" : txtContrasena.getText();

        if (nombre.isEmpty() || clave.isEmpty()) {
            alerta(Alert.AlertType.WARNING, "Escribe tu usuario y contraseña.");
            return;
        }

        usuario u = gestor.login(nombre, clave);
        if (u == null) {
            alerta(Alert.AlertType.ERROR, "Usuario o contraseña incorrectos.");
            return;
        }
        if (u.getRol() != rolSeleccionado) {
            alerta(Alert.AlertType.WARNING,
                    "Ese usuario es " + u.getRol() + ". Selecciona el rol correcto.");
            return;
        }

        Sesion.get().iniciarSesion(u);
        abrir(u.esAdmin() ? "adminproductos" : "catalogo");
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
