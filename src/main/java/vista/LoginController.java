package vista;

import aplicacion.Sesion;
import java.util.Optional;
import vista.util.Recursos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import usuarios.usuario;

/** Controlador de login.fxml */
public class LoginController {

    private static final String ACTIVO =
            "-fx-background-color: #1A56DB; -fx-background-radius: 20; -fx-padding: 4 12 4 12;";
    private static final String INACTIVO =
            "-fx-background-color: #EBF5FF; -fx-background-radius: 20; -fx-padding: 4 12 4 12;";

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
    private ImageView imgLogo;

    @FXML
    private Hyperlink lnkRegistro;

    private final Sesion sesion = new Sesion();
    private boolean modoAdmin = false;

    @FXML
    public void initialize() {
        imgLogo.setImage(Recursos.logo());
        aplicarEstilos();
        txtCorreo.requestFocus();
    }

    @FXML
    public void seleccionarCliente(ActionEvent event) {
        modoAdmin = false;
        aplicarEstilos();
    }

    @FXML
    public void seleccionarAdministrador(ActionEvent event) {
        modoAdmin = true;
        aplicarEstilos();
    }

    @FXML
    public void ingresar(ActionEvent event) {
        String correo = txtCorreo.getText() == null ? "" : txtCorreo.getText().trim();
        String clave = txtContrasena.getText();

        if (correo.isEmpty() || clave == null || clave.isEmpty()) {
            error("Ingresa el correo y la contraseña para continuar.");
            return;
        }

        usuario u = sesion.login(correo, clave);
        if (u == null) {
            error("Correo o contraseña incorrectos.");
            return;
        }
        if (u.esAdmin() != modoAdmin) {
            sesion.cerrarSesion();
            error(u.esAdmin()
                    ? "Ese usuario es administrador: selecciona el modo Administrador."
                    : "Ese usuario es cliente: selecciona el modo Cliente.");
            return;
        }

        App.abrirCatalogo(sesion);
    }

    @FXML
    public void registrar(ActionEvent event) {
        ButtonType crear = new ButtonType("Registrar", ButtonBar.ButtonData.OK_DONE);
        Dialog<Boolean> dialogo = new Dialog<>();
        dialogo.setTitle("UniShop");
        dialogo.setHeaderText("Crear cuenta de cliente");

        TextField correo = new TextField();
        correo.setPromptText("nombre@correo.com");
        PasswordField clave = new PasswordField();
        clave.setPromptText("Mínimo 4 caracteres");

        GridPane contenido = new GridPane();
        contenido.setHgap(10);
        contenido.setVgap(10);
        contenido.setPadding(new Insets(18));
        contenido.add(new Label("Correo:"), 0, 0);
        contenido.add(correo, 1, 0);
        contenido.add(new Label("Contraseña:"), 0, 1);
        contenido.add(clave, 1, 1);
        dialogo.getDialogPane().setContent(contenido);
        dialogo.getDialogPane().getButtonTypes().addAll(crear, ButtonType.CANCEL);

        Button aceptar = (Button) dialogo.getDialogPane().lookupButton(crear);
        aceptar.setDisable(true);
        Runnable validar = () -> aceptar.setDisable(
                correo.getText() == null || correo.getText().trim().isEmpty()
                || clave.getText() == null || clave.getText().length() < 4);
        correo.textProperty().addListener((obs, antiguo, nuevo) -> validar.run());
        clave.textProperty().addListener((obs, antiguo, nuevo) -> validar.run());

        dialogo.setResultConverter(boton -> {
            if (boton != crear) {
                return null;
            }
            return sesion.registrarCliente(correo.getText(), clave.getText());
        });

        Optional<Boolean> resultado = dialogo.showAndWait();
        if (resultado.isPresent()) {
            if (resultado.get()) {
                informacion("Cuenta creada. Ya puedes iniciar sesión como cliente.");
            } else {
                error("No se pudo registrar: el correo ya existe o la contraseña es muy corta.");
            }
        }
    }

    private void aplicarEstilos() {
        btnCliente.setStyle(modoAdmin ? INACTIVO : ACTIVO);
        btnAdmin.setStyle(modoAdmin ? ACTIVO : INACTIVO);
        btnCliente.setTextFill(modoAdmin ? Color.web("#1A56DB") : Color.WHITE);
        btnAdmin.setTextFill(modoAdmin ? Color.WHITE : Color.web("#1A56DB"));
    }

    private void error(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("UniShop");
        alerta.setHeaderText("No se pudo iniciar sesión");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void informacion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("UniShop");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
