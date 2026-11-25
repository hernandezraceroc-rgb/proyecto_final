package vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

/** Controlador de itemproductoadmin.fxml: una fila de la tabla del administrador. */
public class ItemProductoAdminController {

    @FXML
    private Label lblId;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblCantidad;

    @FXML
    private Label lblValor;

    @FXML
    private Hyperlink btnEditar;

    @FXML
    private Hyperlink btnEliminar;

    private Runnable alEditar;
    private Runnable alEliminar;

    public void setDatos(String id, String nombre, String cantidad, String valor,
                         Runnable alEditar, Runnable alEliminar) {
        lblId.setText(id);
        lblNombre.setText(nombre);
        lblCantidad.setText(cantidad);
        lblValor.setText(valor);
        this.alEditar = alEditar;
        this.alEliminar = alEliminar;
    }

    @FXML
    public void editar(ActionEvent event) {
        if (alEditar != null) {
            alEditar.run();
        }
    }

    @FXML
    public void eliminar(ActionEvent event) {
        if (alEliminar != null) {
            alEliminar.run();
        }
    }
}
