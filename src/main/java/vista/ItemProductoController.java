package vista;

import aplicacion.Sesion;
import java.util.Locale;
import vista.util.Recursos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import modelo.producto;

/** Controlador de la tarjeta de producto (itemproducto.fxml). */
public class ItemProductoController {

    @FXML
    private ImageView imgProducto;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblPrecio;

    @FXML
    private Label lblStock;

    @FXML
    private Label lblMensaje;

    @FXML
    private Button btnAgregarCarrito;

    @FXML
    private Button btnAgregarDeseos;

    private producto producto;
    private Sesion sesion;

    public void setProducto(producto p, Sesion sesion) {
        this.producto = p;
        this.sesion = sesion;
        imgProducto.setImage(Recursos.producto(p.getCategoria()));
        lblNombre.setText(p.getNombre());
        lblPrecio.setText(String.format(Locale.US, "$%,.0f", p.getPrecio()));
        lblStock.setText("Stock: " + p.getCantidad() + " uds.");
        lblStock.setTextFill(p.getCantidad() > 0 ? Color.web("#059669") : Color.web("#DC2626"));
        mensaje("");
    }

    @FXML
    public void agregarCarrito(ActionEvent event) {
        if (producto == null || sesion == null) {
            return;
        }
        if (producto.getCantidad() <= 0) {
            mensaje("Sin stock disponible");
            return;
        }
        int yaAgregado = sesion.cantidadEnCarrito(producto);
        if (yaAgregado >= producto.getCantidad()) {
            mensaje("Ya agregaste todo el stock");
            return;
        }
        sesion.getCarrito().agregarProducto(producto, 1);
        mensaje("✓ En el carrito");
    }

    @FXML
    public void agregarDeseos(ActionEvent event) {
        if (producto == null || sesion == null) {
            return;
        }
        if (sesion.getDeseos().contiene(producto)) {
            mensaje("Ya está en tu lista de deseos");
            return;
        }
        sesion.getDeseos().agregarProducto(producto);
        mensaje("✓ Guardado en deseos");
    }

    private void mensaje(String texto) {
        lblMensaje.setText(texto);
        lblMensaje.setTextFill(texto.startsWith("✓")
                ? Color.web("#059669") : Color.web("#DC2626"));
    }
}
