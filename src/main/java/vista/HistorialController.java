package vista;

import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import modelo.Compra;
import usuarios.usuario;

public class HistorialController {

    @FXML
    private TableView<Compra> tblCompras;

    @FXML
    private TableColumn<Compra, String> colFecha;

    @FXML
    private TableColumn<Compra, String> colCliente;

    @FXML
    private TableColumn<Compra, String> colTotal;

    @FXML
    private TableColumn<Compra, String> colArticulos;

    @FXML
    private TextArea txtDetalle;

    @FXML
    private Button btnVolver;

    private final Sesion sesion = Sesion.get();

    public void initialize() {
        colFecha.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getFecha()));
        colCliente.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getCliente()));
        colTotal.setCellValueFactory(d ->
                new ReadOnlyStringWrapper("$ " + String.format("%,.0f", d.getValue().getTotal())));
        colArticulos.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(String.valueOf(d.getValue().getItems().size())));

        tblCompras.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> {
            txtDetalle.setText(actual == null ? "" : actual.getResumen());
        });

        btnVolver.setOnAction(e -> abrir(esAdmin() ? "adminproductos" : "catalogo"));
        cargar();
    }

    private void cargar() {
        usuario actual = sesion.getUsuarioActual();
        ArrayList<Compra> lista = new ArrayList<>();
        for (Compra c : sesion.getHistorial().listar()) {
            if (actual == null || actual.esAdmin()
                    || actual.getNombreUsuario().equalsIgnoreCase(c.getCliente())) {
                lista.add(c);
            }
        }
        tblCompras.setItems(FXCollections.observableArrayList(lista));
    }

    private boolean esAdmin() {
        return sesion.getUsuarioActual() != null && sesion.getUsuarioActual().esAdmin();
    }

    private void abrir(String fxml) {
        try {
            App.setRoot(fxml);
        } catch (IOException ex) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("UniShop");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo abrir la pantalla: " + ex.getMessage());
            alerta.showAndWait();
        }
    }
}
