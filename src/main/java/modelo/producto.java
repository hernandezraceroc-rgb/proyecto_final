package modelo;

import java.util.Objects;

public class producto {

    private String nombre;
    private int cantidad;
    private double valor;

    public producto(String nombre, int cantidad, double valor) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.valor = valor;
    }

// constructor para agragar objetos con cantidad 1 por defecto
    public producto(String nombre, double valor) {
        this(nombre, 1, valor);
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
    
    //metodo alias para que se pueda usar getPrecio y getValor
    public double getPrecio() {
        return valor;
    }

    
    
    @Override
    public String toString() {
        return nombre + " | Cantidad: " + cantidad + " | Valor: $" + valor;
    }

    public String tos() {
        return toString();
    }

    
    //metodos para re-verificar si el producto existe
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        producto producto = (producto) o;
        return Double.compare(producto.valor, valor) == 0 &&
               Objects.equals(nombre, producto.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, valor);
    }
}