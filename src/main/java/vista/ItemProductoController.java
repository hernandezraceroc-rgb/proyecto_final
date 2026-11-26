package vista;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.producto;

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
    private Button btnAgregarCarrito;

    @FXML
    private Button btnAgregarDeseos;

    private producto productoActual;
    private Runnable alCambiar;

    public void initialize() {
        btnAgregarCarrito.setOnAction(e -> agregarAlCarrito());
        btnAgregarDeseos.setOnAction(e -> agregarADeseos());
    }

    public void setProducto(producto p, Runnable alCambiar) {
        this.productoActual = p;
        this.alCambiar = alCambiar;

        cargarImagen(p);
        lblNombre.setText(p.getNombre());
        lblPrecio.setText("$ " + String.format("%,.0f", p.getPrecio()));

        boolean agotado = p.getCantidad() <= 0;
        lblStock.setText(agotado ? "Agotado" : "Stock: " + p.getCantidad() + " uds.");
        lblStock.setStyle("-fx-text-fill: " + (agotado ? "#dc2626" : "#10b981")
                + "; -fx-font-size: 11px;");

        btnAgregarCarrito.setDisable(agotado);

        boolean enDeseos = Sesion.get().getDeseos().contiene(p);
        btnAgregarDeseos.setText(enDeseos ? "En deseos" : "+ Deseos");
        btnAgregarDeseos.setDisable(enDeseos);
    }

    private void cargarImagen(producto p) {
        String archivo = (p.getImagen() == null || p.getImagen().isBlank())
                ? "default.png" : p.getImagen();
        java.net.URL recurso = App.class.getResource(App.RUTA_VISTAS + "img/" + archivo);
        if (recurso == null) {
            recurso = App.class.getResource(App.RUTA_VISTAS + "img/default.png");
        }
        imgProducto.setImage(recurso == null ? null : new Image(recurso.toExternalForm()));
    }

    private void agregarAlCarrito() {
        if (productoActual == null) {
            return;
        }
        if (!Sesion.get().agregarAlCarrito(productoActual, 1)) {
            alerta("No hay más stock disponible de este producto.");
            return;
        }
        if (alCambiar != null) {
            alCambiar.run();
        }
    }

    private void agregarADeseos() {
        if (productoActual == null) {
            return;
        }
        if (Sesion.get().agregarADeseos(productoActual)) {
            btnAgregarDeseos.setText("En deseos");
            btnAgregarDeseos.setDisable(true);
        }
    }

    private void alerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("UniShop");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
