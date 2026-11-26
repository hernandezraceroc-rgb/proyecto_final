package vista;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import modelo.producto;

public class CatalogoController {

    @FXML
    private TextField txtBuscador;

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private GridPane gridProductos;

    @FXML
    private Hyperlink btnCatalogo;

    @FXML
    private Hyperlink btnDeseos;

    @FXML
    private Hyperlink btnHistorial;

    @FXML
    private Hyperlink btnCarrito;

    @FXML
    private Hyperlink btnSalir;

    private final Sesion sesion = Sesion.get();

    public void initialize() {
        if (sesion.getUsuarioActual() != null) {
            lblNombreUsuario.setText(sesion.getUsuarioActual().getNombreUsuario());
        }

        mostrar(sesion.getCatalogo().getProductos());
        actualizarCarrito();

        txtBuscador.textProperty().addListener((obs, anterior, nuevo) ->
                mostrar(sesion.getCatalogo().buscar(nuevo)));

        btnCatalogo.setOnAction(e -> {
            txtBuscador.clear();
            mostrar(sesion.getCatalogo().getProductos());
        });
        btnDeseos.setOnAction(e -> abrir("deseos"));
        btnHistorial.setOnAction(e -> abrir("historial"));
        btnCarrito.setOnAction(e -> abrir("carrito"));
        btnSalir.setOnAction(e -> {
            sesion.cerrarSesion();
            abrir("login");
        });
    }

    @FXML
    public void filtrarCategoria(ActionEvent evento) {
        String categoria = ((Button) evento.getSource()).getText();
        if ("Todos".equals(categoria)) {
            mostrar(sesion.getCatalogo().getProductos());
            return;
        }

        ArrayList<producto> filtrados = new ArrayList<>();
        for (producto p : sesion.getCatalogo().getProductos()) {
            if (categoria.equalsIgnoreCase(p.getCategoria())) {
                filtrados.add(p);
            }
        }
        mostrar(filtrados);
    }

    private void mostrar(List<producto> productos) {
        gridProductos.getChildren().clear();

        if (productos.isEmpty()) {
            Label vacio = new Label("No hay productos que coincidan con la búsqueda.");
            vacio.setStyle("-fx-text-fill: #6b7280;");
            gridProductos.add(vacio, 0, 0);
            GridPane.setColumnSpan(vacio, 2);
            return;
        }

        int columna = 0;
        int fila = 0;
        for (producto p : productos) {
            try {
                FXMLLoader loader = App.cargarLoader("itemproducto");
                Parent tarjeta = loader.load();
                ItemProductoController controlador = loader.getController();
                controlador.setProducto(p, this::actualizarCarrito);
                gridProductos.add(tarjeta, columna, fila);
                columna++;
                if (columna == 2) {
                    columna = 0;
                    fila++;
                }
            } catch (IOException ex) {
                aviso(Alert.AlertType.ERROR, "No se pudo mostrar un producto: " + ex.getMessage());
                return;
            }
        }
    }

    private void actualizarCarrito() {
        btnCarrito.setText("Carrito (" + sesion.getCarrito().getCantidadTotalItems() + ")");
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
