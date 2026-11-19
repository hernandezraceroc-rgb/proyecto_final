package vista;

import aplicacion.Sesion;
import java.io.IOException;
import vista.util.Recursos;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import modelo.NodoProducto;
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
        if (sesion.getCarrito().getCabeza() == null) {
            informacion("Tu carrito está vacío.");
            return;
        }

        StringBuilder texto = new StringBuilder();
        for (NodoProducto n = sesion.getCarrito().getCabeza(); n != null; n = n.getSiguiente()) {
            texto.append(n.getCantidad()).append(" x ")
                 .append(n.getProducto().getNombre())
                 .append("  —  ")
                 .append(String.format(Locale.US, "$%,.0f",
                         n.getProducto().getPrecio() * n.getCantidad()))
                 .append('\n');
        }
        texto.append("\nSubtotal: ")
             .append(String.format(Locale.US, "$%,.0f", sesion.getCarrito().calcularSubtotal()))
             .append("\nEnvío: ")
             .append(String.format(Locale.US, "$%,.0f", sesion.getCarrito().getCostoEnvio()))
             .append("\nTOTAL: ")
             .append(String.format(Locale.US, "$%,.0f", sesion.getCarrito().calcularTotalFinal()));

        ButtonType finalizar = new ButtonType("Finalizar compra", ButtonBar.ButtonData.OK_DONE);
        ButtonType deshacer = new ButtonType("Deshacer última acción", ButtonBar.ButtonData.OTHER);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("UniShop");
        alerta.setHeaderText("Carrito de compras");
        alerta.setContentText(texto.toString());
        alerta.getButtonTypes().setAll(finalizar, deshacer, ButtonType.CANCEL);

        Optional<ButtonType> respuesta = alerta.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == deshacer) {
            sesion.getCarrito().deshacerUltimaAccion();
            abrirCarrito(null);
        } else if (respuesta.isPresent() && respuesta.get() == finalizar) {
            if (sesion.finalizarCompra()) {
                informacion("¡Compra finalizada! El stock se actualizó y quedó en el historial.");
                actualizarProductos();
            } else {
                error("No hay stock suficiente para finalizar la compra.");
            }
        }
    }

    @FXML
    public void abrirDeseos(ActionEvent event) {
        StringBuilder texto = new StringBuilder();
        for (NodoProducto n = sesion.getDeseos().getCabeza(); n != null; n = n.getSiguiente()) {
            texto.append("• ").append(n.getProducto().getNombre()).append('\n');
        }
        if (texto.length() == 0) {
            texto.append("Tu lista de deseos está vacía.");
        }
        informacionLarga("Lista de deseos", texto.toString());
    }

    @FXML
    public void abrirHistorial(ActionEvent event) {
        StringBuilder texto = new StringBuilder();
        for (String compra : sesion.getHistorial()) {
            texto.append(compra).append('\n');
        }
        if (texto.length() == 0) {
            texto.append("Todavía no hay compras registradas.");
        }
        informacionLarga("Historial de compras", texto.toString());
    }

    /** Alta de productos, disponible solo cuando entró un administrador. */
    @FXML
    public void administrarProductos(ActionEvent event) {
        TextField nombre = new TextField();
        nombre.setPromptText("Nombre del producto");
        ChoiceBox<String> categoria = new ChoiceBox<>();
        categoria.getItems().addAll("Tecnología", "Hogar", "Accesorios", "Deportes");
        categoria.setValue("Tecnología");
        TextField cantidad = new TextField();
        cantidad.setPromptText("Ej: 20");
        TextField valor = new TextField();
        valor.setPromptText("Ej: 129900");

        GridPane contenido = new GridPane();
        contenido.setHgap(10);
        contenido.setVgap(10);
        contenido.setPadding(new Insets(16));
        contenido.add(new Label("Nombre:"), 0, 0);
        contenido.add(nombre, 1, 0);
        contenido.add(new Label("Categoría:"), 0, 1);
        contenido.add(categoria, 1, 1);
        contenido.add(new Label("Cantidad:"), 0, 2);
        contenido.add(cantidad, 1, 2);
        contenido.add(new Label("Valor:"), 0, 3);
        contenido.add(valor, 1, 3);

        ButtonType registrar = new ButtonType("Registrar", ButtonBar.ButtonData.OK_DONE);
        Dialog<Boolean> dialogo = new Dialog<>();
        dialogo.setTitle("UniShop - Administración");
        dialogo.setHeaderText("Registrar un producto");
        dialogo.getDialogPane().setContent(contenido);
        dialogo.getDialogPane().getButtonTypes().addAll(registrar, ButtonType.CANCEL);

        Button aceptar = (Button) dialogo.getDialogPane().lookupButton(registrar);
        aceptar.setDisable(true);
        Runnable validar = () -> aceptar.setDisable(
                nombre.getText() == null || nombre.getText().trim().isEmpty()
                || !esEntero(cantidad.getText()) || !esDecimal(valor.getText()));
        nombre.textProperty().addListener((obs, anterior, actual) -> validar.run());
        cantidad.textProperty().addListener((obs, anterior, actual) -> validar.run());
        valor.textProperty().addListener((obs, anterior, actual) -> validar.run());

        dialogo.setResultConverter(boton -> {
            if (boton != registrar) {
                return false;
            }
            return sesion.getCatalogo().registrar(nombre.getText().trim(),
                    categoria.getValue(), Integer.parseInt(cantidad.getText().trim()),
                    Double.parseDouble(valor.getText().trim()));
        });

        Optional<Boolean> resultado = dialogo.showAndWait();
        if (resultado.isPresent()) {
            if (resultado.get()) {
                informacion("Producto registrado en el catálogo.");
                actualizarProductos();
            } else {
                error("No se pudo registrar: revisa los datos o usa otro nombre.");
            }
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

    private boolean esEntero(String texto) {
        try {
            Integer.parseInt(texto == null ? "" : texto.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean esDecimal(String texto) {
        try {
            return Double.parseDouble(texto == null ? "" : texto.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void error(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("UniShop");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void informacion(String mensaje) {
        informacionLarga(null, mensaje);
    }

    private void informacionLarga(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("UniShop");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.getDialogPane().setPrefWidth(420);
        alerta.showAndWait();
    }
}
