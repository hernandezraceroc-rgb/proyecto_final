/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author Admin
 */
public class AdminProductosController {

    @FXML
    private Button btnNavPedidos;
    
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
    private VBox contenedorProductosAdmin;

    public void initialize() {
        // llenaran aqui el ComboBox y la tabla de productos
    }
}
