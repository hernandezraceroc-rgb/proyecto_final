/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
/**
 *
 * @author Admin
 */
public class AdminPedidosController {
    
    @FXML
    private Button btnNavProductos;
    
    @FXML
    private Label lblContadorCola;
    
    @FXML
    private HBox contenedorColaPedidos;
    
    @FXML
    private Label lblTituloSiguiente;
    
    @FXML
    private Label lblDetalleSiguiente;
    
    @FXML
    private Button btnProcesarSiguiente;

    public void initialize() {
        // aqui la cola de pedidos pendientes
    }
}
