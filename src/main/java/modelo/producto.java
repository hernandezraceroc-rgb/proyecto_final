package modelo;

import java.util.Objects;

public class producto {

    private String nombre;
    private int cantidad;
    private double valor;
    private String categoria;
    private String imagen;

    public producto(String nombre, int cantidad, double valor) {
        this(nombre, cantidad, valor, "General");
    }

    // constructor para agragar objetos con cantidad 1 por defecto
    public producto(String nombre, double valor) {
        this(nombre, 1, valor, "General");
    }

    public producto(String nombre, int cantidad, double valor, String categoria) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.valor = valor;
        this.categoria = (categoria == null || categoria.trim().isEmpty()) ? "General" : categoria.trim();
        this.imagen = "default.png";
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

    public String getCategoria() {
        return (categoria == null || categoria.trim().isEmpty()) ? "General" : categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    // metodo alias para que se pueda usar getPrecio y getValor
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

    // El nombre identifica al producto dentro del catalogo
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof producto)) {
            return false;
        }
        producto otro = (producto) o;
        return nombre != null && nombre.equalsIgnoreCase(otro.nombre);
    }

    @Override
    public int hashCode() {
        return nombre == null ? 0 : nombre.toLowerCase().hashCode();
    }
}
