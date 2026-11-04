package modelo;

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
        NodoProducto actual = cabeza;
        while (actual != null) {
            if (actual.getProducto().equals(p)) {
                historialAcciones.apilar(new Accion(Accion.TipoAccion.INCREMENTAR, p, actual.getCantidad()));
                actual.setCantidad(actual.getCantidad() + cantidad);
                return;
            }
            actual = actual.getSiguiente();
        }

        NodoProducto nuevo = new NodoProducto(p, cantidad);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        tamaño++;

        historialAcciones.apilar(new Accion(Accion.TipoAccion.AGREGAR, p, 0));
    }

    // Botón "+"
    public void incrementarCantidad(producto p) {
        NodoProducto actual = cabeza;
        while (actual != null) {
            if (actual.getProducto().equals(p)) {
                historialAcciones.apilar(new Accion(Accion.TipoAccion.INCREMENTAR, p, actual.getCantidad()));
                actual.setCantidad(actual.getCantidad() + 1);
                return;
            }
            actual = actual.getSiguiente();
        }
    }

    // Botón "-"
    public void decrementarCantidad(producto p) {
        NodoProducto actual = cabeza;
        while (actual != null) {
            if (actual.getProducto().equals(p)) {
                historialAcciones.apilar(new Accion(Accion.TipoAccion.DECREMENTAR, p, actual.getCantidad()));
                if (actual.getCantidad() > 1) {
                    actual.setCantidad(actual.getCantidad() - 1);
                } else {
                    eliminarProductoSinHistorial(p);
                }
                return;
            }
            actual = actual.getSiguiente();
        }
    }

    // Botón "Quitar"
    public boolean eliminarProducto(producto p) {
        NodoProducto actual = cabeza;
        while (actual != null) {
            if (actual.getProducto().equals(p)) {
                historialAcciones.apilar(new Accion(Accion.TipoAccion.ELIMINAR, p, actual.getCantidad()));
                return eliminarProductoSinHistorial(p);
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    private boolean eliminarProductoSinHistorial(producto p) {
        if (cabeza == null) return false;

        if (cabeza.getProducto().equals(p)) {
            cabeza = cabeza.getSiguiente();
            tamaño--;
            return true;
        }

        NodoProducto actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getProducto().equals(p)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tamaño--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    // Botón "Deshacer última acción"
    public boolean deshacerUltimaAccion() {
        if (historialAcciones.estaVacia()) {
            System.out.println("No hay acciones para deshacer.");
            return false;
        }

        Accion ultimaAccion = historialAcciones.desapilar();
        producto p = ultimaAccion.getProducto();

        switch (ultimaAccion.getTipo()) {
            case AGREGAR:
                eliminarProductoSinHistorial(p);
                break;
            case ELIMINAR:
                NodoProducto nuevo = new NodoProducto(p, ultimaAccion.getCantidadPrevia());
                nuevo.setSiguiente(cabeza);
                cabeza = nuevo;
                tamaño++;
                break;
            case INCREMENTAR:
            case DECREMENTAR:
                NodoProducto actual = cabeza;
                while (actual != null) {
                    if (actual.getProducto().equals(p)) {
                        actual.setCantidad(ultimaAccion.getCantidadPrevia());
                        break;
                    }
                    actual = actual.getSiguiente();
                }
                break;
        }
        return true;
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