/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 *
 * @author Admin
 */
public class ItemHistorialController {

    @FXML
    private Label lblPedido;

    @FXML
    private Label lblFecha;

    @FXML
    private Label lblProductos;

    @FXML
    private Label lblTotal;

    @FXML
    private Label lblEstado;

    @FXML
    private Label lblEtiquetaExtra;

    public void setDatos(String pedido, String fecha, String productos, String total, String estado, boolean esTopePila) {
        lblPedido.setText(pedido);
        lblFecha.setText(fecha);
        lblProductos.setText(productos);
        lblTotal.setText(total);
        lblEstado.setText(estado);

        if (estado.equalsIgnoreCase("En camino")) {
            // (En camino)
            lblEstado.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #D97706; -fx-background-radius: 15; -fx-padding: 4 10 4 10; -fx-font-size: 11px; -fx-font-weight: bold;");
        } else {
            // (Entregado)
            lblEstado.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #16A34A; -fx-background-radius: 15; -fx-padding: 4 10 4 10; -fx-font-size: 11px; -fx-font-weight: bold;");
        }

        lblEtiquetaExtra.setVisible(esTopePila);
        lblEtiquetaExtra.setManaged(esTopePila);
    }
}
