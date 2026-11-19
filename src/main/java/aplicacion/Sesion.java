package aplicacion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import modelo.Carrito;
import modelo.ListaDeseos;
import modelo.NodoProducto;
import modelo.catalogo;
import modelo.producto;
import usuarios.gestorusuarios;
import usuarios.usuario;

/**
 * Estado compartido por las pantallas: usuario, catálogo, carrito, deseos e historial.
 * Se crea una instancia al abrir el login y se pasa al catálogo.
 */
public class Sesion {

    private final gestorusuarios gestorUsuarios = new gestorusuarios();
    private final catalogo catalogo = new catalogo();
    private final Carrito carrito = new Carrito();
    private final ListaDeseos deseos = new ListaDeseos();
    private final ArrayList<String> historial = new ArrayList<>();
    private usuario usuarioActual;

    public Sesion() {
        // Cliente de prueba para poder entrar sin registrarse: cliente@unishop.com / 1234
        gestorUsuarios.registrarCliente("cliente@unishop.com", "1234");
        cargarProductosDeEjemplo();
    }

    private void cargarProductosDeEjemplo() {
        catalogo.registrar("Audífonos Bluetooth Pro", "Tecnología", 24, 129900);
        catalogo.registrar("Teclado Mecánico RGB", "Tecnología", 12, 219900);
        catalogo.registrar("Monitor 27\" 4K", "Tecnología", 8, 1299900);
        catalogo.registrar("Smartwatch Fit Pro", "Tecnología", 15, 499900);
        catalogo.registrar("Lámpara de escritorio LED", "Hogar", 30, 89900);
        catalogo.registrar("Organizador de escritorio", "Hogar", 18, 45900);
        catalogo.registrar("Set de sábanas 400 hilos", "Hogar", 10, 189900);
        catalogo.registrar("Cafetera de goteo", "Hogar", 14, 249900);
        catalogo.registrar("Mochila urbana", "Accesorios", 20, 159900);
        catalogo.registrar("Cargador rápido 65W", "Accesorios", 40, 79900);
        catalogo.registrar("Gorra unisex", "Accesorios", 25, 59900);
        catalogo.registrar("Estuche para laptop", "Accesorios", 16, 119900);
        catalogo.registrar("Botella térmica 750 ml", "Deportes", 35, 39900);
        catalogo.registrar("Mat de yoga antideslizante", "Deportes", 12, 99900);
        catalogo.registrar("Mancuernas ajustables 10 kg", "Deportes", 9, 349900);
        catalogo.registrar("Balón de fútbol profesional", "Deportes", 20, 139900);
    }

    public usuario login(String correo, String clave) {
        usuarioActual = gestorUsuarios.login(correo, clave);
        return usuarioActual;
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }

    public boolean registrarCliente(String correo, String clave) {
        return gestorUsuarios.registrarCliente(correo, clave);
    }

    public boolean esAdmin() {
        return usuarioActual != null && usuarioActual.esAdmin();
    }

    /** Cantidad de un producto que ya hay en el carrito. */
    public int cantidadEnCarrito(producto p) {
        int total = 0;
        for (NodoProducto n = carrito.getCabeza(); n != null; n = n.getSiguiente()) {
            if (n.getProducto().equals(p)) {
                total += n.getCantidad();
            }
        }
        return total;
    }

    /**
     * Confirma la compra: valida stock, lo descuenta y guarda el resumen en el historial.
     *
     * @return false si el carrito está vacío o no hay stock suficiente.
     */
    public boolean finalizarCompra() {
        if (carrito.getCabeza() == null) {
            return false;
        }

        Map<producto, Integer> unidades = new LinkedHashMap<>();
        for (NodoProducto n = carrito.getCabeza(); n != null; n = n.getSiguiente()) {
            unidades.merge(n.getProducto(), n.getCantidad(), Integer::sum);
        }
        for (Map.Entry<producto, Integer> e : unidades.entrySet()) {
            if (e.getValue() > e.getKey().getCantidad()) {
                return false;
            }
        }

        StringBuilder detalle = new StringBuilder();
        for (Map.Entry<producto, Integer> e : unidades.entrySet()) {
            catalogo.descontarStock(e.getKey().getNombre(), e.getValue());
            detalle.append(e.getValue()).append(" x ").append(e.getKey().getNombre()).append(", ");
        }
        if (detalle.length() > 2) {
            detalle.setLength(detalle.length() - 2);
        }

        String sello = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        historial.add(0, sello + " | " + detalle
                + " | Total: $" + String.format("%,.0f", carrito.calcularTotalFinal()));
        carrito.vaciar();
        return true;
    }

    public usuario getUsuario() {
        return usuarioActual;
    }

    public gestorusuarios getGestorUsuarios() {
        return gestorUsuarios;
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

    public ArrayList<String> getHistorial() {
        return historial;
    }
}
