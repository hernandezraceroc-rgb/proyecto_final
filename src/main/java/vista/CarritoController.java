package vista;

import java.io.IOException;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import modelo.Carrito;
import modelo.Compra;
import modelo.NodoProducto;

public class CarritoController {

    @FXML
    private TableView<NodoProducto> tblItems;

    @FXML
    private TableColumn<NodoProducto, String> colProducto;

    @FXML
    private TableColumn<NodoProducto, String> colPrecio;

    @FXML
    private TableColumn<NodoProducto, String> colCantidad;

    @FXML
    private TableColumn<NodoProducto, String> colSubtotal;

    @FXML
    private Label lblSubtotal;

    @FXML
    private Label lblEnvio;

    @FXML
    private Label lblTotal;

    @FXML
    private Button btnMas;

    @FXML
    private Button btnMenos;

    @FXML
    private Button btnQuitar;

    @FXML
    private Button btnDeshacer;

    @FXML
    private Button btnFinalizar;

    @FXML
    private Button btnVolver;

    private final Sesion sesion = Sesion.get();

    public void initialize() {
        colProducto.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(d.getValue().getProducto().getNombre()));
        colPrecio.setCellValueFactory(d ->
                new ReadOnlyStringWrapper("$ " + formato(d.getValue().getProducto().getPrecio())));
        colCantidad.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(String.valueOf(d.getValue().getCantidad())));
        colSubtotal.setCellValueFactory(d ->
                new ReadOnlyStringWrapper("$ " + formato(
                        d.getValue().getProducto().getPrecio() * d.getValue().getCantidad())));

        btnMas.setOnAction(e -> cambiarCantidad(true));
        btnMenos.setOnAction(e -> cambiarCantidad(false));
        btnQuitar.setOnAction(e -> quitar());
        btnDeshacer.setOnAction(e -> deshacer());
        btnFinalizar.setOnAction(e -> finalizar());
        btnVolver.setOnAction(e -> abrir("catalogo"));

        refrescar();
    }

    private void refrescar() {
        Carrito carrito = sesion.getCarrito();
        tblItems.setItems(FXCollections.observableArrayList(carrito.getItems()));
        lblSubtotal.setText("$ " + formato(carrito.calcularSubtotal()));
        lblEnvio.setText("$ " + formato(carrito.getCostoEnvio()));
        lblTotal.setText("$ " + formato(carrito.calcularTotalFinal()));
        btnFinalizar.setDisable(carrito.getCabeza() == null);
        btnDeshacer.setDisable(!carrito.puedeDeshacer());
    }

    private NodoProducto seleccionado() {
        NodoProducto nodo = tblItems.getSelectionModel().getSelectedItem();
        if (nodo == null) {
            aviso(Alert.AlertType.WARNING, "Selecciona un producto de la lista.");
        }
        return nodo;
    }

    private void cambiarCantidad(boolean sumar) {
        NodoProducto nodo = seleccionado();
        if (nodo == null) {
            return;
        }

        if (sumar) {
            if (nodo.getCantidad() >= nodo.getProducto().getCantidad()) {
                aviso(Alert.AlertType.WARNING, "No hay más stock disponible.");
                return;
            }
            sesion.getCarrito().incrementarCantidad(nodo.getProducto());
        } else {
            sesion.getCarrito().decrementarCantidad(nodo.getProducto());
        }
        refrescar();
    }

    private void quitar() {
        NodoProducto nodo = seleccionado();
        if (nodo == null) {
            return;
        }
        sesion.getCarrito().eliminarProducto(nodo.getProducto());
        refrescar();
    }

    private void deshacer() {
        if (!sesion.getCarrito().deshacerUltimaAccion()) {
            aviso(Alert.AlertType.WARNING, "No hay acciones para deshacer.");
        }
        refrescar();
    }

    private void finalizar() {
        Compra compra = sesion.finalizarCompra();
        if (compra == null) {
            aviso(Alert.AlertType.ERROR,
                    "No se pudo finalizar: no hay stock suficiente o el carrito está vacío.");
            return;
        }
        aviso(Alert.AlertType.INFORMATION,
                "Compra registrada por $ " + formato(compra.getTotal())
                + ".\nEl pedido quedó en la cola del administrador.");
        refrescar();
    }

    private String formato(double valor) {
        return String.format("%,.0f", valor);
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
