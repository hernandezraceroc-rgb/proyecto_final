package vista;

import java.io.IOException;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import modelo.Pedido;

public class AdminPedidosController {

    @FXML
    private TableView<Pedido> tblPedidos;

    @FXML
    private TableColumn<Pedido, String> colId;

    @FXML
    private TableColumn<Pedido, String> colFecha;

    @FXML
    private TableColumn<Pedido, String> colCliente;

    @FXML
    private TableColumn<Pedido, String> colTotal;

    @FXML
    private TableColumn<Pedido, String> colArticulos;

    @FXML
    private TextArea txtDetalle;

    @FXML
    private Button btnProcesar;

    @FXML
    private Button btnVolver;

    @FXML
    private Button btnSalir;

    private final Sesion sesion = Sesion.get();

    public void initialize() {
        colId.setCellValueFactory(d -> new ReadOnlyStringWrapper(String.valueOf(d.getValue().getId())));
        colFecha.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getFecha()));
        colCliente.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getCliente()));
        colTotal.setCellValueFactory(d ->
                new ReadOnlyStringWrapper("$ " + String.format("%,.0f", d.getValue().getTotal())));
        colArticulos.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(String.valueOf(d.getValue().getItems().size())));

        tblPedidos.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> {
            txtDetalle.setText(actual == null ? "" : actual.getResumen());
        });

        btnProcesar.setOnAction(e -> procesar());
        btnVolver.setOnAction(e -> abrir("adminproductos"));
        btnSalir.setOnAction(e -> {
            sesion.cerrarSesion();
            abrir("login");
        });

        refrescar();
    }

    private void refrescar() {
        tblPedidos.setItems(FXCollections.observableArrayList(sesion.getPedidos().listar()));
        btnProcesar.setDisable(sesion.getPedidos().estaVacia());
        txtDetalle.clear();
    }

    private void procesar() {
        if (sesion.getPedidos().estaVacia()) {
            aviso(Alert.AlertType.INFORMATION, "No hay pedidos pendientes.");
            return;
        }

        Pedido siguiente = sesion.getPedidos().frente();
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Procesar el pedido #" + siguiente.getId() + " de " + siguiente.getCliente() + "?",
                ButtonType.OK, ButtonType.CANCEL);
        confirmacion.setTitle("UniShop");
        confirmacion.setHeaderText("Se descontará el stock de los productos.");
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        Pedido procesado = sesion.procesarSiguientePedido();
        if (procesado == null) {
            aviso(Alert.AlertType.ERROR, "No se pudo procesar el pedido.");
            return;
        }
        refrescar();
        aviso(Alert.AlertType.INFORMATION,
                "Pedido #" + procesado.getId() + " procesado. Stock actualizado.");
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
