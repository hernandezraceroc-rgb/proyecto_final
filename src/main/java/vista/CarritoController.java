package vista;

import aplicacion.Sesion;
import java.io.IOException;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import modelo.Carrito;
import modelo.NodoProducto;

/** Controlador de carrito.fxml: items, resumen y acciones de la compra. */
public class CarritoController {

    private static final String RUTA_ITEM =
            "/com/mycompany/proyecto_final_virtual/itemcarrito.fxml";

    @FXML
    private VBox contenedorItemsCarrito;

    @FXML
    private Label lblSubtotal;

    @FXML
    private Label lblEnvio;

    @FXML
    private Label lblTotal;

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private Hyperlink lnkCatalogo;

    @FXML
    private Hyperlink lnkDeseos;

    @FXML
    private Hyperlink lnkHistorial;

    @FXML
    private Hyperlink btnCarrito;

    @FXML
    private Button btnFinalizarCompra;

    @FXML
    private Button btnSeguirComprando;

    @FXML
    private Button btnDeshacer;

    private Sesion sesion;

    @FXML
    public void initialize() {
        btnFinalizarCompra.setDisable(true);
        btnDeshacer.setDisable(true);
    }

    /** Lo llama App al abrir la pantalla del carrito. */
    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
        lblNombreUsuario.setText(sesion.getUsuario() == null
                ? "Invitado" : sesion.getUsuario().getNombreUsuario());
        actualizarCarrito();
    }

    @FXML
    public void finalizarCompra(ActionEvent event) {
        if (sesion == null) {
            return;
        }
        if (sesion.getCarrito().getCabeza() == null) {
            aviso(Alert.AlertType.INFORMATION, "Carrito vacío",
                    "Agrega productos antes de finalizar la compra.");
            return;
        }
        if (sesion.finalizarCompra()) {
            actualizarCarrito();
            aviso(Alert.AlertType.INFORMATION, "¡Compra finalizada!",
                    "El stock se actualizó y la compra quedó en el historial.");
        } else {
            aviso(Alert.AlertType.ERROR, "No se pudo finalizar la compra",
                    "No hay stock suficiente para los productos del carrito.");
        }
    }

    @FXML
    public void deshacer(ActionEvent event) {
        if (sesion != null && sesion.getCarrito().deshacerUltimaAccion()) {
            actualizarCarrito();
        }
    }

    @FXML
    public void seguirComprando(ActionEvent event) {
        volverAlCatalogo();
    }

    @FXML
    public void abrirCatalogo(ActionEvent event) {
        volverAlCatalogo();
    }

    @FXML
    public void abrirDeseos(ActionEvent event) {
        if (sesion != null) {
            App.abrirDeseos(sesion);
        }
    }

    @FXML
    public void abrirHistorial(ActionEvent event) {
        if (sesion != null) {
            App.abrirHistorial(sesion);
        }
    }

    @FXML
    public void abrirCarrito(ActionEvent event) {
        if (sesion != null) {
            App.abrirCarrito(sesion);
        }
    }

    private void volverAlCatalogo() {
        if (sesion != null) {
            App.abrirCatalogo(sesion);
        }
    }

    /** Vuelve a construir la lista y el resumen a partir del estado del carrito. */
    private void actualizarCarrito() {
        if (sesion == null) {
            return;
        }

        Carrito carrito = sesion.getCarrito();
        boolean vacio = carrito.getCabeza() == null;

        contenedorItemsCarrito.getChildren().clear();
        if (vacio) {
            Label mensaje = new Label("Tu carrito está vacío.");
            mensaje.setTextFill(Color.web("#6B7280"));
            contenedorItemsCarrito.getChildren().add(mensaje);
        } else {
            for (NodoProducto nodo = carrito.getCabeza();
                 nodo != null;
                 nodo = nodo.getSiguiente()) {
                contenedorItemsCarrito.getChildren().add(crearItem(nodo));
            }
        }

        lblSubtotal.setText(dinero(carrito.calcularSubtotal()));
        lblEnvio.setText(vacio ? "Gratis" : dinero(carrito.getCostoEnvio()));
        lblTotal.setText(dinero(carrito.calcularTotalFinal()));
        btnFinalizarCompra.setDisable(vacio);
        btnDeshacer.setDisable(!carrito.puedeDeshacer());
    }

    private Node crearItem(NodoProducto nodo) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(RUTA_ITEM));
            Node vistaItem = loader.load();
            ItemCarritoController item = loader.getController();
            item.setDatos(nodo.getProducto(), nodo.getCantidad(), sesion, this::actualizarCarrito);
            return vistaItem;
        } catch (IOException e) {
            e.printStackTrace();
            Label error = new Label("No se pudo cargar " + nodo.getProducto().getNombre());
            error.setTextFill(Color.web("#DC2626"));
            return error;
        }
    }

    private static String dinero(double valor) {
        return String.format(Locale.US, "$%,.0f", valor);
    }

    private static void aviso(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("UniShop");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
