/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
/**
 *
 * @author Admin
 */
public class ItemPedidoController {
   
    @FXML
    private VBox boxTarjeta;
    
    @FXML
    private Label lblIdPedido;
    
    @FXML
    private Label lblCliente;
    
    @FXML
    private Label lblTotal;
    
    @FXML
    private Label lblFlecha;

    public void setDatos(String id, String cliente, String total, boolean esPrimero, boolean esUltimo) {
        lblIdPedido.setText(id);
        lblCliente.setText(cliente);
        lblTotal.setText(total);

        // para resaltar el primer pedido de la cola en azul claro como en figma
        if (esPrimero) {
            boxTarjeta.setStyle("-fx-background-color: #EFF6FF; -fx-background-radius: 10; -fx-border-color: #3B82F6; -fx-border-width: 1.5; -fx-border-radius: 10; -fx-padding: 14;");
        }

        // para ocultar la flecha derecha si es el ultimo pedido de la fila
        lblFlecha.setVisible(!esUltimo);
        lblFlecha.setManaged(!esUltimo);
    }
}
