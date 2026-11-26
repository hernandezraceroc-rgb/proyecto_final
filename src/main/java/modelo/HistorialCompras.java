package modelo;

import java.util.ArrayDeque;
import java.util.ArrayList;

// Pila (LIFO): la compra más reciente queda arriba
public class HistorialCompras {

    private final ArrayDeque<Compra> pila = new ArrayDeque<>();

    public static HistorialCompras desdeLista(ArrayList<Compra> lista) {
        HistorialCompras historial = new HistorialCompras();
        if (lista != null) {
            for (Compra c : lista) {
                historial.pila.addLast(c);
            }
        }
        return historial;
    }

    public void apilar(Compra compra) {
        if (compra != null) {
            pila.addFirst(compra);
        }
    }

    public Compra cima() {
        return pila.peekFirst();
    }

    public Compra desapilar() {
        return pila.pollFirst();
    }

    public ArrayList<Compra> listar() {
        return new ArrayList<>(pila);
    }

    public ArrayList<Compra> toList() {
        return listar();
    }

    public int getTamaño() {
        return pila.size();
    }

    public boolean estaVacio() {
        return pila.isEmpty();
    }
}
