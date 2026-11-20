/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
/**
 *
 * @author Admin
 */
public class ItemCarritoController {
    
    @FXML
    private ImageView imgProducto;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblPrecioUnitario;

    @FXML
    private Hyperlink btnQuitar;

    @FXML
    private Button btnMenos;

    @FXML
    private Label lblCantidad;

    @FXML
    private Button btnMas;

    @FXML
    private Label lblSubtotalItem;

    public void setDatos(String nombre, String precioUnitario, String cantidad, String subtotal) {
        lblNombre.setText(nombre);
        lblPrecioUnitario.setText(precioUnitario);
        lblCantidad.setText(cantidad);
        lblSubtotalItem.setText(subtotal);
    }
}
