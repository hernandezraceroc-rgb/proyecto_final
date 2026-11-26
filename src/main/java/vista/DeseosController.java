package vista;

import java.io.IOException;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import modelo.producto;

public class DeseosController {

    @FXML
    private TableView<producto> tblDeseos;

    @FXML
    private TableColumn<producto, String> colNombre;

    @FXML
    private TableColumn<producto, String> colCategoria;

    @FXML
    private TableColumn<producto, String> colPrecio;

    @FXML
    private TableColumn<producto, String> colStock;

    @FXML
    private Button btnPasar;

    @FXML
    private Button btnQuitar;

    @FXML
    private Button btnVolver;

    private final Sesion sesion = Sesion.get();

    public void initialize() {
        colNombre.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getNombre()));
        colCategoria.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getCategoria()));
        colPrecio.setCellValueFactory(d ->
                new ReadOnlyStringWrapper("$ " + String.format("%,.0f", d.getValue().getPrecio())));
        colStock.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(String.valueOf(d.getValue().getCantidad())));

        btnPasar.setOnAction(e -> pasarAlCarrito());
        btnQuitar.setOnAction(e -> quitar());
        btnVolver.setOnAction(e -> abrir("catalogo"));

        refrescar();
    }

    private void refrescar() {
        tblDeseos.setItems(FXCollections.observableArrayList(sesion.getDeseos().listar()));
    }

    private producto seleccionado() {
        producto p = tblDeseos.getSelectionModel().getSelectedItem();
        if (p == null) {
            aviso(Alert.AlertType.WARNING, "Selecciona un producto de la lista.");
        }
        return p;
    }

    private void pasarAlCarrito() {
        producto p = seleccionado();
        if (p == null) {
            return;
        }
        if (!sesion.agregarAlCarrito(p, 1)) {
            aviso(Alert.AlertType.WARNING, "No hay stock disponible de este producto.");
            return;
        }
        sesion.getDeseos().eliminarProducto(p);
        sesion.guardarDeseos();
        refrescar();
        aviso(Alert.AlertType.INFORMATION, "Producto agregado al carrito.");
    }

    private void quitar() {
        producto p = seleccionado();
        if (p == null) {
            return;
        }
        sesion.getDeseos().eliminarProducto(p);
        sesion.guardarDeseos();
        refrescar();
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
