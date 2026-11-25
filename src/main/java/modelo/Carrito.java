package modelo;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private NodoProducto cabeza;
    private int tamaño;
    private final double COSTO_ENVIO = 8000.0;
    private PilaAcciones historialAcciones;

    public Carrito() {
        this.cabeza = null;
        this.tamaño = 0;
        this.historialAcciones = new PilaAcciones();
    }

    // Agregar producto al carrito
    public void agregarProducto(producto p, int cantidad) {
        if (!cabeEnCarrito(p, cantidad)) {
            return;
        }

        NodoProducto actual = buscar(p);
        if (actual != null) {
            historialAcciones.apilar(new Accion(Accion.TipoAccion.INCREMENTAR, p, actual.getCantidad()));
            actual.setCantidad(actual.getCantidad() + cantidad);
            return;
        }

        NodoProducto nuevo = new NodoProducto(p, cantidad);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        tamaño++;

        historialAcciones.apilar(new Accion(Accion.TipoAccion.AGREGAR, p, 0));
    }

    // Botón "+"
    public void incrementarCantidad(producto p) {
        NodoProducto actual = buscar(p);
        if (actual == null || !cabeEnCarrito(p, 1)) {
            return;
        }
        historialAcciones.apilar(new Accion(Accion.TipoAccion.INCREMENTAR, p, actual.getCantidad()));
        actual.setCantidad(actual.getCantidad() + 1);
    }

    // Botón "-"
    public void decrementarCantidad(producto p) {
        NodoProducto actual = buscar(p);
        if (actual == null) {
            return;
        }
        historialAcciones.apilar(new Accion(Accion.TipoAccion.DECREMENTAR, p, actual.getCantidad()));
        if (actual.getCantidad() > 1) {
            actual.setCantidad(actual.getCantidad() - 1);
        } else {
            eliminarProductoSinHistorial(p);
        }
    }

    // Botón "Quitar"
    public boolean eliminarProducto(producto p) {
        NodoProducto actual = buscar(p);
        if (actual == null) {
            return false;
        }
        historialAcciones.apilar(new Accion(Accion.TipoAccion.ELIMINAR, p, actual.getCantidad()));
        return eliminarProductoSinHistorial(p);
    }

    private boolean eliminarProductoSinHistorial(producto p) {
        NodoProducto anterior = null;
        NodoProducto actual = cabeza;
        while (actual != null && !actual.getProducto().equals(p)) {
            anterior = actual;
            actual = actual.getSiguiente();
        }
        if (actual == null) {
            return false;
        }
        if (anterior == null) {
            cabeza = actual.getSiguiente();
        } else {
            anterior.setSiguiente(actual.getSiguiente());
        }
        tamaño--;
        return true;
    }

    private NodoProducto buscar(producto p) {
        if (p == null) {
            return null;
        }
        for (NodoProducto actual = cabeza; actual != null; actual = actual.getSiguiente()) {
            if (actual.getProducto().equals(p)) {
                return actual;
            }
        }
        return null;
    }

    /** Unidades de un producto en el carrito (hay como máximo un nodo por producto). */
    public int cantidadDe(producto p) {
        NodoProducto actual = buscar(p);
        return actual == null ? 0 : actual.getCantidad();
    }

    /** El carrito nunca puede superar el stock disponible del producto. */
    private boolean cabeEnCarrito(producto p, int unidades) {
        return p != null && unidades > 0 && cantidadDe(p) + unidades <= p.getCantidad();
    }

    /** Quita del historial las acciones de un producto que ya no existe en el catálogo. */
    public void descartarAccionesDe(producto p) {
        if (p == null || historialAcciones.estaVacia()) {
            return;
        }
        List<Accion> conservadas = new ArrayList<>();
        while (!historialAcciones.estaVacia()) {
            Accion accion = historialAcciones.desapilar();
            if (!p.equals(accion.getProducto())) {
                conservadas.add(accion);
            }
        }
        for (int i = conservadas.size() - 1; i >= 0; i--) {
            historialAcciones.apilar(conservadas.get(i));
        }
    }

    // Botón "Deshacer última acción"
    public boolean puedeDeshacer() {
        return !historialAcciones.estaVacia();
    }

    public boolean deshacerUltimaAccion() {
        if (historialAcciones.estaVacia()) {
            return false;
        }

        Accion ultimaAccion = historialAcciones.desapilar();
        producto p = ultimaAccion.getProducto();

        switch (ultimaAccion.getTipo()) {
            case AGREGAR:
                eliminarProductoSinHistorial(p);
                break;
            case ELIMINAR:
            case INCREMENTAR:
            case DECREMENTAR:
                restaurarCantidad(p, ultimaAccion.getCantidadPrevia());
                break;
        }
        return true;
    }

    /** Restaura la cantidad previa, reinsertando el nodo si la acción lo había eliminado. */
    private void restaurarCantidad(producto p, int cantidadPrevia) {
        if (p == null) {
            return;
        }
        int cantidad = Math.min(cantidadPrevia, p.getCantidad());
        if (cantidad <= 0) {
            return;
        }

        NodoProducto actual = buscar(p);
        if (actual != null) {
            actual.setCantidad(cantidad);
            return;
        }

        NodoProducto nuevo = new NodoProducto(p, cantidad);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        tamaño++;
    }

    // Cálculos para la barra lateral
    public double calcularSubtotal() {
        double subtotal = 0;
        NodoProducto actual = cabeza;
        while (actual != null) {
            subtotal += actual.getProducto().getPrecio() * actual.getCantidad();
            actual = actual.getSiguiente();
        }
        return subtotal;
    }

    public double getCostoEnvio() {
        return cabeza == null ? 0 : COSTO_ENVIO;
    }

    public double calcularTotalFinal() {
        if (cabeza == null) return 0;
        return calcularSubtotal() + COSTO_ENVIO;
    }

    public int getCantidadTotalItems() {
        int total = 0;
        NodoProducto actual = cabeza;
        while (actual != null) {
            total += actual.getCantidad();
            actual = actual.getSiguiente();
        }
        return total;
    }

    public void vaciar() {
        cabeza = null;
        tamaño = 0;
        historialAcciones = new PilaAcciones();
    }

    public NodoProducto getCabeza() { return cabeza; }
    public int getTamaño() { return tamaño; }
}
