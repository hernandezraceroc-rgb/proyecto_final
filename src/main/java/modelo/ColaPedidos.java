package modelo;

import java.util.ArrayDeque;
import java.util.ArrayList;

// Cola (FIFO): el pedido más antiguo se atiende primero
public class ColaPedidos {

    private final ArrayDeque<Pedido> cola = new ArrayDeque<>();

    public static ColaPedidos desdeLista(ArrayList<Pedido> lista) {
        ColaPedidos colaPedidos = new ColaPedidos();
        if (lista != null) {
            for (Pedido p : lista) {
                colaPedidos.cola.addLast(p);
            }
        }
        return colaPedidos;
    }

    public void encolar(Pedido pedido) {
        if (pedido != null) {
            cola.addLast(pedido);
        }
    }

    public Pedido desencolar() {
        return cola.pollFirst();
    }

    public Pedido frente() {
        return cola.peekFirst();
    }

    public ArrayList<Pedido> listar() {
        return new ArrayList<>(cola);
    }

    public ArrayList<Pedido> toList() {
        return listar();
    }

    public int getTamaño() {
        return cola.size();
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }
}
