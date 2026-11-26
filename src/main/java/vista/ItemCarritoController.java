package vista;

import aplicacion.Sesion;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import modelo.producto;
import vista.util.Recursos;

/** Controlador de itemcarrito.fxml: una fila del carrito. */
public class ItemCarritoController {

    @FXML
    private ImageView imgProducto;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblPrecioUnitario;

    @FXML
    private Hyperlink btnQuitar;

    @FXML
    private Button btnMenos;

    @FXML
    private Label lblCantidad;

    @FXML
    private Button btnMas;

    @FXML
    private Label lblSubtotalItem;

    private producto producto;
    private Sesion sesion;
    private Runnable alCambiar;

    /** Carga los datos de la fila y la acción a ejecutar cuando cambie el carrito. */
    public void setDatos(producto p, int cantidad, Sesion sesion, Runnable alCambiar) {
        this.producto = p;
        this.sesion = sesion;
        this.alCambiar = alCambiar;

        imgProducto.setImage(Recursos.producto(p.getCategoria()));
        lblNombre.setText(p.getNombre());
        lblPrecioUnitario.setText("Unitario: " + dinero(p.getPrecio()));
        lblCantidad.setText(String.valueOf(cantidad));
        lblSubtotalItem.setText(dinero(p.getPrecio() * cantidad));
        btnMas.setDisable(sesion.cantidadEnCarrito(p) >= p.getCantidad());
    }

    @FXML
    public void incrementar(ActionEvent event) {
        if (!listo() || sesion.cantidadEnCarrito(producto) >= producto.getCantidad()) {
            return;
        }
        sesion.getCarrito().incrementarCantidad(producto);
        alCambiar.run();
    }

    @FXML
    public void decrementar(ActionEvent event) {
        if (!listo()) {
            return;
        }
        sesion.getCarrito().decrementarCantidad(producto);
        alCambiar.run();
    }

    @FXML
    public void quitar(ActionEvent event) {
        if (!listo()) {
            return;
        }
        sesion.getCarrito().eliminarProducto(producto);
        alCambiar.run();
    }

    private boolean listo() {
        return producto != null && sesion != null && alCambiar != null;
    }

    private static String dinero(double valor) {
        return String.format(Locale.US, "$%,.0f", valor);
    }
}
