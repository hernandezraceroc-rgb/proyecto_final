package modelo;

// Representa cada acción realizada en el carrito
public class Accion {
    public enum TipoAccion { AGREGAR, ELIMINAR, INCREMENTAR, DECREMENTAR }

    private TipoAccion tipo;
    private producto producto;
    private int cantidadPrevia;

    public Accion(TipoAccion tipo, producto producto, int cantidadPrevia){
        this.tipo = tipo;
        this.producto = producto;
        this.cantidadPrevia = cantidadPrevia;
    }

    public TipoAccion getTipo(){ 
        return tipo;
    }
    public producto getProducto(){
        return producto; 
    }
    public int getCantidadPrevia(){ 
        return cantidadPrevia; 
    }
}