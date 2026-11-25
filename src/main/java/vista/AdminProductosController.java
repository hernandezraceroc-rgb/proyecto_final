package vista;

import aplicacion.Sesion;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import modelo.producto;

/** Controlador de adminproductos.fxml: alta, edición y borrado de productos. */
public class AdminProductosController {

    private static final String RUTA_ITEM =
            "/com/mycompany/proyecto_final_virtual/itemproductoadmin.fxml";
    private static final String[] CATEGORIAS =
            {"Tecnología", "Hogar", "Accesorios", "Deportes", "General"};

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtValor;

    @FXML
    private ComboBox<String> cbCategoria;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnNavPedidos;

    @FXML
    private Hyperlink lnkCancelarEdicion;

    @FXML
    private Label lblMensaje;

    @FXML
    private VBox contenedorProductosAdmin;

    private Sesion sesion;
    private producto productoEnEdicion;

    @FXML
    public void initialize() {
        cbCategoria.getItems().addAll(CATEGORIAS);
        cbCategoria.getSelectionModel().selectFirst();
        reiniciarEdicion();
    }

    /** Lo llama App al abrir el panel de administración. */
    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
        limpiarFormulario();
        actualizarProductos();
    }

    @FXML
    public void guardarProducto(ActionEvent event) {
        if (sesion == null) {
            return;
        }
        String nombre = texto(txtNombre.getText());
        String categoria = cbCategoria.getValue();
        Integer cantidad = entero(txtCantidad.getText());
        Double valor = decimal(txtValor.getText());

        if (nombre.isEmpty()) {
            mensaje("Escribe el nombre del producto.", true);
            return;
        }
        if (cantidad == null || cantidad < 0) {
            mensaje("La cantidad debe ser un entero mayor o igual a 0.", true);
            return;
        }
        if (valor == null || valor <= 0) {
            mensaje("El valor debe ser un número mayor a 0.", true);
            return;
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            mensaje("Elige una categoría.", true);
            return;
        }

        boolean correcto;
        if (productoEnEdicion == null) {
            correcto = sesion.getCatalogo().registrar(nombre, categoria, cantidad, valor);
            mensaje(correcto ? "Producto registrado en el catálogo."
                    : "Ya existe un producto con ese nombre.", !correcto);
        } else {
            correcto = sesion.getCatalogo().editar(
                    productoEnEdicion.getNombre(), cantidad, valor);
            if (correcto) {
                productoEnEdicion.setCategoria(categoria);
            }
            mensaje(correcto ? "Producto actualizado."
                    : "No se pudo actualizar el producto.", !correcto);
        }

        if (correcto) {
            limpiarFormulario();
            reiniciarEdicion();
            actualizarProductos();
        }
    }

    @FXML
    public void cancelarEdicion(ActionEvent event) {
        limpiarFormulario();
        reiniciarEdicion();
        mensaje("", false);
    }

    @FXML
    public void volverAlCatalogo(ActionEvent event) {
        if (sesion != null) {
            App.abrirCatalogo(sesion);
        }
    }

    @FXML
    public void verPedidos(ActionEvent event) {
        aviso(Alert.AlertType.INFORMATION, "Pedidos pendientes",
                "La gestión de pedidos estará disponible en la siguiente etapa.");
    }

    private void editar(producto p) {
        productoEnEdicion = p;
        txtNombre.setText(p.getNombre());
        txtNombre.setDisable(true);
        txtCantidad.setText(String.valueOf(p.getCantidad()));
        txtValor.setText(String.valueOf(p.getValor()));
        cbCategoria.getSelectionModel().select(p.getCategoria());
        btnGuardar.setText("Guardar cambios");
        lnkCancelarEdicion.setVisible(true);
        lnkCancelarEdicion.setManaged(true);
        mensaje("Editando \"" + p.getNombre() + "\". El nombre no se puede cambiar.", false);
    }

    private void eliminar(producto p) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("UniShop");
        confirmacion.setHeaderText("Eliminar producto");
        confirmacion.setContentText("¿Eliminar \"" + p.getNombre() + "\" del catálogo?");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isEmpty() || respuesta.get() != ButtonType.OK) {
            return;
        }

        sesion.getCatalogo().eliminar(p.getNombre());
        sesion.getCarrito().eliminarProducto(p);
        sesion.getCarrito().descartarAccionesDe(p);
        sesion.getDeseos().eliminarProducto(p);
        if (p == productoEnEdicion) {
            limpiarFormulario();
            reiniciarEdicion();
        }
        mensaje("Producto eliminado.", false);
        actualizarProductos();
    }

    private void actualizarProductos() {
        if (sesion == null) {
            return;
        }
        contenedorProductosAdmin.getChildren().clear();
        List<producto> productos = sesion.getCatalogo().getProductos();
        if (productos.isEmpty()) {
            Label vacio = new Label("No hay productos registrados.");
            vacio.setTextFill(Color.web("#6B7280"));
            contenedorProductosAdmin.getChildren().add(vacio);
            return;
        }
        for (int i = 0; i < productos.size(); i++) {
            contenedorProductosAdmin.getChildren().add(crearFila(i + 1, productos.get(i)));
        }
    }

    private Node crearFila(int id, producto p) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(RUTA_ITEM));
            Node fila = loader.load();
            ItemProductoAdminController item = loader.getController();
            item.setDatos(String.format("%02d", id), p.getNombre(),
                    String.valueOf(p.getCantidad()), dinero(p.getValor()),
                    () -> editar(p), () -> eliminar(p));
            return fila;
        } catch (IOException e) {
            e.printStackTrace();
            Label error = new Label("No se pudo cargar " + p.getNombre());
            error.setTextFill(Color.web("#DC2626"));
            return error;
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtCantidad.clear();
        txtValor.clear();
        cbCategoria.getSelectionModel().selectFirst();
        mensaje("", false);
    }

    private void reiniciarEdicion() {
        productoEnEdicion = null;
        txtNombre.setDisable(false);
        btnGuardar.setText("Guardar producto");
        lnkCancelarEdicion.setVisible(false);
        lnkCancelarEdicion.setManaged(false);
    }

    private void mensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.setTextFill(esError ? Color.web("#DC2626") : Color.web("#059669"));
    }

    private static String texto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private static Integer entero(String valor) {
        try {
            return Integer.valueOf(texto(valor));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Double decimal(String valor) {
        try {
            return Double.valueOf(texto(valor));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String dinero(double valor) {
        return String.format(Locale.US, "$%,.0f", valor);
    }

    private static void aviso(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("UniShop");
        alerta.setHeaderText(titulo);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
