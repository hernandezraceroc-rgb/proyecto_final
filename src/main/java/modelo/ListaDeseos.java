package modelo;

import java.util.ArrayList;

public class ListaDeseos {
    private NodoProducto cabeza;
    private int tamaño;

    public ListaDeseos() {
        this.cabeza = null;
        this.tamaño = 0;
    }

    public void agregarProducto(producto p) {
        if (p == null || contiene(p)) return;
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

    public ArrayList<producto> listar() {
        ArrayList<producto> lista = new ArrayList<>();
        NodoProducto actual = cabeza;
        while (actual != null) {
            lista.add(actual.getProducto());
            actual = actual.getSiguiente();
        }
        return lista;
    }

    public void vaciar() {
        cabeza = null;
        tamaño = 0;
    }

    public NodoProducto getCabeza() { return cabeza; }
    public int getTamaño() { return tamaño; }
}
