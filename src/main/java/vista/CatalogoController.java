package vista;

import aplicacion.Sesion;
import java.io.IOException;
import vista.util.Recursos;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import modelo.producto;

/** Controlador de catalogo.fxml */
public class CatalogoController {

    private static final String SELECCIONADO =
            "-fx-background-radius: 20; -fx-background-color: #1A56DB; -fx-padding: 4 12 4 12;";
    private static final String NO_SELECCIONADO =
            "-fx-background-radius: 20; -fx-background-color: white; -fx-padding: 4 12 4 12;";

    @FXML
    private TextField txtBuscador;

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private GridPane gridProductos;

    @FXML
    private Hyperlink btnCarrito;

    @FXML
    private Hyperlink lnkCatalogo;

    @FXML
    private Hyperlink lnkDeseos;

    @FXML
    private Hyperlink lnkHistorial;

    @FXML
    private Hyperlink lnkAdmin;

    @FXML
    private ImageView imgLogo;

    @FXML
    private Button btnTodos;

    @FXML
    private Button btnTecnologia;

    @FXML
    private Button btnHogar;

    @FXML
    private Button btnAccesorios;

    @FXML
    private Button btnDeportes;

    private Sesion sesion;
    private String categoriaActiva = "Todos";

    @FXML
    public void initialize() {
        imgLogo.setImage(Recursos.logo());
        txtBuscador.textProperty().addListener((obs, anterior, actual) -> actualizarProductos());
        resaltarCategoria(categoriaActiva);
    }

    /** Lo llama App al pasar del login al catálogo. */
    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
        lblNombreUsuario.setText(sesion.getUsuario().getNombreUsuario());
        boolean admin = sesion.esAdmin();
        lnkAdmin.setVisible(admin);
        lnkAdmin.setManaged(admin);
        actualizarProductos();
    }

    @FXML
    public void filtrarCategoria(ActionEvent event) {
        categoriaActiva = ((Button) event.getSource()).getText();
        resaltarCategoria(categoriaActiva);
        actualizarProductos();
    }

    @FXML
    public void mostrarCatalogo(ActionEvent event) {
        categoriaActiva = "Todos";
        txtBuscador.clear();
        resaltarCategoria(categoriaActiva);
        actualizarProductos();
    }

    @FXML
    public void abrirCarrito(ActionEvent event) {
        App.abrirCarrito(sesion);
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

    /** Abre el panel de administración, disponible solo para el administrador. */
    @FXML
    public void administrarProductos(ActionEvent event) {
        if (sesion != null) {
            App.abrirAdminProductos(sesion);
        }
    }

    private void actualizarProductos() {
        if (sesion == null) {
            return;
        }
        gridProductos.getChildren().clear();

        List<producto> productos = "Todos".equals(categoriaActiva)
                ? new ArrayList<>(sesion.getCatalogo().getProductos())
                : sesion.getCatalogo().filtrarPorCategoria(categoriaActiva);

        String busqueda = txtBuscador.getText();
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            String q = busqueda.trim().toLowerCase();
            productos.removeIf(p -> !p.getNombre().toLowerCase().contains(q)
                    && !p.getCategoria().toLowerCase().contains(q));
        }

        if (productos.isEmpty()) {
            Label vacio = new Label("No hay productos que coincidan con la búsqueda.");
            vacio.setTextFill(Color.web("#6B7280"));
            GridPane.setColumnSpan(vacio, 2);
            gridProductos.add(vacio, 0, 0);
            return;
        }

        int columna = 0;
        int fila = 0;
        for (producto p : productos) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        App.class.getResource("/com/mycompany/proyecto_final_virtual/itemproducto.fxml"));
                VBox tarjeta = loader.load();
                ItemProductoController item = loader.getController();
                item.setProducto(p, sesion);
                gridProductos.add(tarjeta, columna, fila);
                columna++;
                if (columna == 2) {
                    columna = 0;
                    fila++;
                }
            } catch (IOException e) {
                e.printStackTrace();
                error("No se pudo cargar la tarjeta del producto: " + e.getMessage());
                return;
            }
        }
    }

    private void resaltarCategoria(String categoria) {
        Button[] botones = {btnTodos, btnTecnologia, btnHogar, btnAccesorios, btnDeportes};
        for (Button b : botones) {
            boolean seleccionado = b.getText().equals(categoria);
            b.setStyle(seleccionado ? SELECCIONADO : NO_SELECCIONADO);
            b.setTextFill(seleccionado ? Color.WHITE : Color.web("#1F2937"));
        }
    }

    private void error(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("UniShop");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
