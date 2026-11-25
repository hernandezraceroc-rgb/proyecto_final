package vista;

import aplicacion.Sesion;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/** Controlador de historial.fxml: compras ya realizadas, la más reciente arriba. */
public class HistorialController {

    private static final String RUTA_ITEM =
            "/com/mycompany/proyecto_final_virtual/itemhistorial.fxml";

    @FXML
    private TextField txtBuscador;

    @FXML
    private VBox contenedorPedidos;

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
        txtBuscador.textProperty().addListener((obs, anterior, actual) -> actualizarHistorial());
    }

    /** Lo llama App al abrir la pantalla de historial. */
    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
        lblNombreUsuario.setText(sesion.getUsuario() == null
                ? "Invitado" : sesion.getUsuario().getNombreUsuario());
        actualizarHistorial();
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

    private void actualizarHistorial() {
        if (sesion == null) {
            return;
        }
        contenedorPedidos.getChildren().clear();

        String busqueda = txtBuscador.getText();
        String consulta = busqueda == null ? "" : busqueda.trim().toLowerCase();
        List<String> compras = sesion.getHistorial();
        int total = compras.size();

        for (int i = 0; i < total; i++) {
            FilaHistorial fila = parsear(compras.get(i), total - i);
            if (!fila.coincide(consulta)) {
                continue;
            }
            contenedorPedidos.getChildren().add(crearItem(fila, i == 0));
        }

        if (contenedorPedidos.getChildren().isEmpty()) {
            Label mensaje = new Label(consulta.isEmpty()
                    ? "Todavía no hay compras registradas."
                    : "No hay compras que coincidan con la búsqueda.");
            mensaje.setTextFill(Color.web("#6B7280"));
            contenedorPedidos.getChildren().add(mensaje);
        }
    }

    /** Separa el resumen guardado por Sesion en las columnas de la tabla. */
    static FilaHistorial parsear(String linea, int numero) {
        String[] partes = linea == null ? new String[0] : linea.split(" \\| ", 3);
        String fecha = partes.length > 0 ? partes[0].trim() : "";
        String productos = partes.length > 1 ? partes[1].trim() : "";
        String total = "-";
        if (partes.length > 2) {
            String cola = partes[2].trim();
            total = cola.startsWith("Total:")
                    ? cola.substring("Total:".length()).trim()
                    : cola;
        }
        return new FilaHistorial(String.format("#%03d", numero), fecha, productos, total);
    }

    private Node crearItem(FilaHistorial fila, boolean esMasReciente) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(RUTA_ITEM));
            Node filaVista = loader.load();
            ItemHistorialController item = loader.getController();
            item.setDatos(fila.pedido, fila.fecha, fila.productos, fila.total,
                    "Entregado", esMasReciente);
            return filaVista;
        } catch (IOException e) {
            e.printStackTrace();
            Label error = new Label("No se pudo cargar el pedido " + fila.pedido);
            error.setTextFill(Color.web("#DC2626"));
            return error;
        }
    }

    /** Una fila ya dividida en columnas; no introduce Pedido todavía. */
    static final class FilaHistorial {
        final String pedido;
        final String fecha;
        final String productos;
        final String total;

        FilaHistorial(String pedido, String fecha, String productos, String total) {
            this.pedido = pedido;
            this.fecha = fecha;
            this.productos = productos;
            this.total = total;
        }

        boolean coincide(String consulta) {
            if (consulta.isEmpty()) {
                return true;
            }
            return pedido.toLowerCase().contains(consulta)
                    || fecha.toLowerCase().contains(consulta)
                    || productos.toLowerCase().contains(consulta)
                    || total.toLowerCase().contains(consulta);
        }
    }
}
