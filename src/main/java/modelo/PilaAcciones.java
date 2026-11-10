package modelo;

// Pila enlazada (LIFO) para almacenar el historial de acciones
public class PilaAcciones {

    private class NodoAccion {
        Accion accion;
        NodoAccion siguiente;

        NodoAccion(Accion accion){
            this.accion = accion;
            this.siguiente = null;
        }
    }

    private NodoAccion tope;

    public PilaAcciones(){
        this.tope = null;
    }

    public void apilar(Accion accion){
        NodoAccion nuevo = new NodoAccion(accion);
        nuevo.siguiente = tope;
        tope = nuevo;
    }

    public Accion desapilar(){
        if (estaVacia()) return null;
        Accion accion = tope.accion;
        tope = tope.siguiente;
        return accion;
    }

    public boolean estaVacia(){
        return tope == null;
    }
}