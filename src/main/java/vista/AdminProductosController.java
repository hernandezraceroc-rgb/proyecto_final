package vista;

import java.io.IOException;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import modelo.producto;

public class AdminProductosController {

    private static final String[] CATEGORIAS = {
        "Tecnología", "Hogar", "Accesorios", "Deportes", "General"
    };

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtValor;

    @FXML
    private ComboBox<String> cbCategoria;

    @FXML
    private TableView<producto> tblProductos;

    @FXML
    private TableColumn<producto, String> colNombre;

    @FXML
    private TableColumn<producto, String> colCategoria;

    @FXML
    private TableColumn<producto, String> colCantidad;

    @FXML
    private TableColumn<producto, String> colValor;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnPedidos;

    @FXML
    private Button btnSalir;

    @FXML
    private Label lblUsuario;

    private final Sesion sesion = Sesion.get();
    private producto seleccionado;

    public void initialize() {
        cbCategoria.getItems().addAll(CATEGORIAS);
        cbCategoria.setValue("General");

        colNombre.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getNombre()));
        colCategoria.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getCategoria()));
        colCantidad.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(String.valueOf(d.getValue().getCantidad())));
        colValor.setCellValueFactory(d ->
                new ReadOnlyStringWrapper("$ " + String.format("%,.0f", d.getValue().getPrecio())));

        tblProductos.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> {
            if (actual != null) {
                seleccionado = actual;
                txtNombre.setText(actual.getNombre());
                txtCantidad.setText(String.valueOf(actual.getCantidad()));
                txtValor.setText(String.valueOf(actual.getPrecio()));
                cbCategoria.setValue(actual.getCategoria());
            }
        });

        btnAgregar.setOnAction(e -> agregar());
        btnEditar.setOnAction(e -> editar());
        btnEliminar.setOnAction(e -> eliminar());
        btnLimpiar.setOnAction(e -> limpiar());
        btnPedidos.setOnAction(e -> abrir("adminpedidos"));
        btnSalir.setOnAction(e -> {
            sesion.cerrarSesion();
            abrir("login");
        });

        if (sesion.getUsuarioActual() != null) {
            lblUsuario.setText("Admin: " + sesion.getUsuarioActual().getNombreUsuario());
        }
        refrescar();
    }

    private void refrescar() {
        tblProductos.setItems(FXCollections.observableArrayList(sesion.getCatalogo().getProductos()));
        tblProductos.refresh();
    }

    private void agregar() {
        int cantidad = entero(txtCantidad.getText());
        double valor = decimal(txtValor.getText());
        String nombre = texto(txtNombre.getText());
        String categoria = cbCategoria.getValue() == null ? "General" : cbCategoria.getValue();

        if (nombre.isEmpty() || cantidad < 0 || valor <= 0) {
            aviso(Alert.AlertType.WARNING,
                    "Completa nombre, cantidad (0 o más) y un valor mayor que cero.");
            return;
        }
        if (!sesion.getCatalogo().registrar(nombre, cantidad, valor, categoria)) {
            aviso(Alert.AlertType.ERROR, "Ya existe un producto con ese nombre.");
            return;
        }
        limpiar();
        refrescar();
    }

    private void editar() {
        if (seleccionado == null) {
            aviso(Alert.AlertType.WARNING, "Selecciona un producto de la tabla.");
            return;
        }
        int cantidad = entero(txtCantidad.getText());
        double valor = decimal(txtValor.getText());
        String nombre = texto(txtNombre.getText());
        String categoria = cbCategoria.getValue() == null ? "General" : cbCategoria.getValue();

        if (nombre.isEmpty() || cantidad < 0 || valor <= 0) {
            aviso(Alert.AlertType.WARNING,
                    "Completa nombre, cantidad (0 o más) y un valor mayor que cero.");
            return;
        }
        if (!sesion.getCatalogo().actualizar(seleccionado, nombre, cantidad, valor, categoria)) {
            aviso(Alert.AlertType.ERROR, "Ese nombre ya lo usa otro producto.");
            return;
        }
        limpiar();
        refrescar();
    }

    private void eliminar() {
        if (seleccionado == null) {
            aviso(Alert.AlertType.WARNING, "Selecciona un producto de la tabla.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar \"" + seleccionado.getNombre() + "\"?",
                ButtonType.OK, ButtonType.CANCEL);
        confirmacion.setTitle("UniShop");
        confirmacion.setHeaderText(null);
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        sesion.getCatalogo().eliminar(seleccionado.getNombre());
        limpiar();
        refrescar();
    }

    private void limpiar() {
        seleccionado = null;
        txtNombre.clear();
        txtCantidad.clear();
        txtValor.clear();
        cbCategoria.setValue("General");
        tblProductos.getSelectionModel().clearSelection();
    }

    private String texto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private int entero(String valor) {
        try {
            return Integer.parseInt(texto(valor));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double decimal(String valor) {
        try {
            return Double.parseDouble(texto(valor));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void abrir(String fxml) {
        try {
            App.setRoot(fxml);
        } catch (IOException ex) {
            aviso(Alert.AlertType.ERROR, "No se pudo abrir la pantalla: " + ex.getMessage());
        }
    }

    private void aviso(Alert.AlertType tipo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("UniShop");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
