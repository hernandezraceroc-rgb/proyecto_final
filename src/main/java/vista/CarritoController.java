/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
/**
 *
 * @author Admin
 */
public class CarritoController {
    
    @FXML
    private VBox contenedorItemsCarrito;

    @FXML
    private Label lblSubtotal;

    @FXML
    private Label lblEnvio;

    @FXML
    private Label lblTotal;

    @FXML
    private Button btnFinalizarCompra;

    @FXML
    private Button btnSeguirComprando;

    @FXML
    private Button btnDeshacer;

    public void initialize() {
        // carga de los productos agregados al carrito
    }
}
