package vista;

import aplicacion.Sesion;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import modelo.NodoProducto;

/** Controlador de deseos.fxml: lista de deseos con búsqueda y acciones por tarjeta. */
public class DeseosController {

    private static final String RUTA_ITEM =
            "/com/mycompany/proyecto_final_virtual/itemdeseo.fxml";

    @FXML
    private TextField txtBuscador;

    @FXML
    private GridPane gridDeseos;

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

    private Sesion sesion;

    @FXML
    public void initialize() {
        txtBuscador.textProperty().addListener((obs, anterior, actual) -> actualizarDeseos());
    }

    /** Lo llama App al abrir la pantalla de deseos. */
    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
        lblNombreUsuario.setText(sesion.getUsuario() == null
                ? "Invitado" : sesion.getUsuario().getNombreUsuario());
        actualizarDeseos();
    }

    @FXML
    public void abrirCatalogo(ActionEvent event) {
        if (sesion != null) {
            App.abrirCatalogo(sesion);
        }
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

    /** Vuelve a construir la cuadrícula a partir del estado real de la lista. */
    private void actualizarDeseos() {
        if (sesion == null) {
            return;
        }
        gridDeseos.getChildren().clear();

        String busqueda = txtBuscador.getText();
        String consulta = busqueda == null ? "" : busqueda.trim().toLowerCase();

        int columna = 0;
        int fila = 0;
        for (NodoProducto nodo = sesion.getDeseos().getCabeza();
             nodo != null;
             nodo = nodo.getSiguiente()) {
            if (!coincide(nodo, consulta)) {
                continue;
            }
            gridDeseos.add(crearItem(nodo.getProducto()), columna, fila);
            columna++;
            if (columna == 2) {
                columna = 0;
                fila++;
            }
        }

        if (gridDeseos.getChildren().isEmpty()) {
            Label mensaje = new Label(consulta.isEmpty()
                    ? "Tu lista de deseos está vacía."
                    : "No hay deseos que coincidan con la búsqueda.");
            mensaje.setTextFill(Color.web("#6B7280"));
            GridPane.setColumnSpan(mensaje, 2);
            gridDeseos.add(mensaje, 0, 0);
        }
    }

    private boolean coincide(NodoProducto nodo, String consulta) {
        if (consulta.isEmpty()) {
            return true;
        }
        String nombre = nodo.getProducto().getNombre();
        String categoria = nodo.getProducto().getCategoria();
        return (nombre != null && nombre.toLowerCase().contains(consulta))
                || (categoria != null && categoria.toLowerCase().contains(consulta));
    }

    private Node crearItem(modelo.producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(RUTA_ITEM));
            Node tarjeta = loader.load();
            ItemDeseoController item = loader.getController();
            item.setDatos(producto, sesion, this::actualizarDeseos);
            return tarjeta;
        } catch (IOException e) {
            e.printStackTrace();
            Label error = new Label("No se pudo cargar " + producto.getNombre());
            error.setTextFill(Color.web("#DC2626"));
            return error;
        }
    }
}
