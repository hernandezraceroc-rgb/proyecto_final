/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
/**
 *
 * @author Admin
 */
public class ItemDeseoController {
    
    @FXML
    private ImageView imgProducto;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblPrecio;

    @FXML
    private Label lblStock;

    @FXML
    private Button btnAgregarCarrito;

    @FXML
    private Button btnQuitar;

    public void setDatos(String nombre, String precio, String stock) {
        lblNombre.setText(nombre);
        lblPrecio.setText(precio);
        lblStock.setText(stock);
    }
}
