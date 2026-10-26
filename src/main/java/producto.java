/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author herna
 */
public class producto {
    
     private String nombre;
    private int cantidad;
    private double valor;
 
    public producto(String nombre, int cantidad, double valor) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.valor = valor;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
 
    public int getCantidad() {
        return cantidad;
    }
 
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
 
    public double getValor() {
        return valor;
    }
 
    public void setValor(double valor) {
        this.valor = valor;
    }
 
    public String tos() {
        return nombre + " | Cantidad: " + cantidad + " | Valor: $" + valor;
    }
    
}
