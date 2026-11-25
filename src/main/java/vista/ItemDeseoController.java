package vista;

import aplicacion.Sesion;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import modelo.producto;
import vista.util.Recursos;

/** Controlador de itemdeseo.fxml: una tarjeta de la lista de deseos. */
public class ItemDeseoController {

    @FXML
    private ImageView imgProducto;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblPrecio;

    @FXML
    private Label lblStock;

    @FXML
    private Button btnAgregarCarrito;

    @FXML
    private Button btnQuitar;

    private producto producto;
    private Sesion sesion;
    private Runnable alCambiar;

    /** Carga los datos de la tarjeta y la acción a ejecutar cuando cambie la lista. */
    public void setDatos(producto p, Sesion sesion, Runnable alCambiar) {
        this.producto = p;
        this.sesion = sesion;
        this.alCambiar = alCambiar;

        imgProducto.setImage(Recursos.producto(p.getCategoria()));
        lblNombre.setText(p.getNombre());
        lblPrecio.setText(dinero(p.getPrecio()));
        lblStock.setText("Stock: " + p.getCantidad() + " uds.");
        lblStock.setTextFill(p.getCantidad() > 0 ? Color.web("#059669") : Color.web("#DC2626"));
        btnAgregarCarrito.setDisable(p.getCantidad() <= 0
                || sesion.cantidadEnCarrito(p) >= p.getCantidad());
    }

    /** Pasa el deseo al carrito y lo quita de la lista. */
    @FXML
    public void agregarCarrito(ActionEvent event) {
        if (!listo() || producto.getCantidad() <= 0) {
            return;
        }
        if (sesion.cantidadEnCarrito(producto) >= producto.getCantidad()) {
            return;
        }
        sesion.getCarrito().agregarProducto(producto, 1);
        sesion.getDeseos().eliminarProducto(producto);
        alCambiar.run();
    }

    @FXML
    public void quitar(ActionEvent event) {
        if (!listo()) {
            return;
        }
        sesion.getDeseos().eliminarProducto(producto);
        alCambiar.run();
    }

    private boolean listo() {
        return producto != null && sesion != null && alCambiar != null;
    }

    private static String dinero(double valor) {
        return String.format(Locale.US, "$%,.0f", valor);
    }
}
