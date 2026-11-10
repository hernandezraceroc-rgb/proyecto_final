package modelo;

public class NodoProducto {
    private producto producto;
    private int cantidad;
    private NodoProducto siguiente;

    public NodoProducto(producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.siguiente = null;
    }

    public NodoProducto(producto producto) {
        this(producto, 1);
    }

    public producto getProducto() { return producto; }
    public void setProducto(producto producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public NodoProducto getSiguiente() { return siguiente; }
    public void setSiguiente(NodoProducto siguiente) { this.siguiente = siguiente; }
}