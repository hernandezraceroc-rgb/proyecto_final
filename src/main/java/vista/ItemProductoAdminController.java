/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
/**
 *
 * @author Admin
 */
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

    public void setDatos(String id, String nombre, String cantidad, String valor) {
        lblId.setText(id);
        lblNombre.setText(nombre);
        lblCantidad.setText(cantidad);
        lblValor.setText(valor);
    }
}
