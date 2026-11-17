/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
/**
 *
 * @author Admin
 */
public class CatalogoController {
    
    @FXML
    private TextField txtBuscador;

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private GridPane gridProductos;

    @FXML
    private Hyperlink btnCarrito;

    public void initialize() {
        // carga de los productos en el gridProductos
    }
}