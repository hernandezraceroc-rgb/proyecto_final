package vista;

import java.util.ArrayList;
import modelo.Carrito;
import modelo.ColaPedidos;
import modelo.Compra;
import modelo.HistorialCompras;
import modelo.ItemCompra;
import modelo.ListaDeseos;
import modelo.NodoProducto;
import modelo.Pedido;
import modelo.catalogo;
import modelo.producto;
import persistencia.GestorJSON;
import usuarios.usuario;

/**
 * Estado compartido de la aplicación: catálogo, carrito, deseos, historial y
 * pedidos. Así todas las pantallas trabajan sobre los mismos datos.
 */
public class Sesion {

    private static Sesion instancia;

    private usuario usuarioActual;
    private final catalogo catalogo;
    private final Carrito carrito;
    private final ListaDeseos deseos;
    private final HistorialCompras historial;
    private final ColaPedidos pedidos;

    private Sesion() {
        catalogo = new catalogo();
        carrito = new Carrito();
        deseos = new ListaDeseos();

        // Se resuelven contra el catálogo actual para no quedarse con stock viejo
        for (producto guardado : GestorJSON.cargarDeseos()) {
            producto actual = catalogo.buscarPorNombre(guardado.getNombre());
            if (actual != null) {
                deseos.agregarProducto(actual);
            }
        }

        historial = HistorialCompras.desdeLista(GestorJSON.cargarHistorial());
        pedidos = ColaPedidos.desdeLista(GestorJSON.cargarPedidos());
    }

    public static Sesion get() {
        if (instancia == null) {
            instancia = new Sesion();
        }
        return instancia;
    }

    public usuario getUsuarioActual() {
        return usuarioActual;
    }

    public catalogo getCatalogo() {
        return catalogo;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public ListaDeseos getDeseos() {
        return deseos;
    }

    public HistorialCompras getHistorial() {
        return historial;
    }

    public ColaPedidos getPedidos() {
        return pedidos;
    }

    public void iniciarSesion(usuario u) {
        usuarioActual = u;
        carrito.vaciar();
    }

    public void cerrarSesion() {
        usuarioActual = null;
        carrito.vaciar();
    }

    public void guardarDeseos() {
        GestorJSON.guardarDeseos(deseos.listar());
    }

    public boolean agregarAlCarrito(producto p, int cantidad) {
        if (p == null || cantidad <= 0) {
            return false;
        }
        int enCarrito = carrito.getCantidad(p);
        if (enCarrito + cantidad > p.getCantidad()) {
            return false;
        }
        carrito.agregarProducto(p, cantidad);
        return true;
    }

    public boolean agregarADeseos(producto p) {
        if (p == null || deseos.contiene(p)) {
            return false;
        }
        deseos.agregarProducto(p);
        guardarDeseos();
        return true;
    }

    /**
     * Valida el stock, registra la compra en el historial (pila) y encola el
     * pedido. El stock se descuenta cuando el administrador procesa el pedido.
     */
    public Compra finalizarCompra() {
        if (carrito.getCabeza() == null) {
            return null;
        }

        ArrayList<ItemCompra> items = new ArrayList<>();
        NodoProducto actual = carrito.getCabeza();
        while (actual != null) {
            producto enCatalogo = catalogo.buscarPorNombre(actual.getProducto().getNombre());
            if (enCatalogo == null || enCatalogo.getCantidad() < actual.getCantidad()) {
                return null;
            }
            items.add(new ItemCompra(enCatalogo.getNombre(), actual.getCantidad(), enCatalogo.getPrecio()));
            actual = actual.getSiguiente();
        }

        String cliente = usuarioActual == null ? "Invitado" : usuarioActual.getNombreUsuario();
        Compra compra = new Compra(cliente, items, carrito.calcularTotalFinal());

        historial.apilar(compra);
        GestorJSON.guardarHistorial(historial.toList());

        Pedido pedido = new Pedido(siguienteIdPedido(), compra.getFecha(), cliente, items, compra.getTotal());
        pedidos.encolar(pedido);
        GestorJSON.guardarPedidos(pedidos.toList());

        carrito.vaciar();
        return compra;
    }

    public int siguienteIdPedido() {
        int mayor = 0;
        for (Pedido p : pedidos.listar()) {
            mayor = Math.max(mayor, p.getId());
        }
        return mayor + 1;
    }

    /**
     * Atiende el pedido más antiguo (dequeue) y descuenta el stock.
     */
    public Pedido procesarSiguientePedido() {
        Pedido pedido = pedidos.desencolar();
        if (pedido == null) {
            return null;
        }
        for (ItemCompra item : pedido.getItems()) {
            producto p = catalogo.buscarPorNombre(item.getNombre());
            if (p != null) {
                p.setCantidad(Math.max(0, p.getCantidad() - item.getCantidad()));
            }
        }
        GestorJSON.guardarProductos(catalogo.getProductos());
        GestorJSON.guardarPedidos(pedidos.toList());
        return pedido;
    }
}
