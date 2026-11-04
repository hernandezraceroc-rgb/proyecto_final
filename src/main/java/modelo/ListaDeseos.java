package modelo;

public class ListaDeseos {
    private NodoProducto cabeza;
    private int tamaño;

    public ListaDeseos() {
        this.cabeza = null;
        this.tamaño = 0;
    }

    public void agregarProducto(producto p) {
        if (contiene(p)) return;
        NodoProducto nuevo = new NodoProducto(p);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        tamaño++;
    }

    public boolean eliminarProducto(producto p) {
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

    public boolean contiene(producto p) {
        NodoProducto actual = cabeza;
        while (actual != null) {
            if (actual.getProducto().equals(p)) return true;
            actual = actual.getSiguiente();
        }
        return false;
    }

    public NodoProducto getCabeza() { return cabeza; }
    public int getTamaño() { return tamaño; }
}