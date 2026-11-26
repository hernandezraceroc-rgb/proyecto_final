package modelo;

import persistencia.GestorJSON;
import java.util.ArrayList;

/**
 *
 * @author herna
 */
public class catalogo {

    private ArrayList<producto> productos;

    public catalogo() {
        productos = GestorJSON.cargarProductos();
    }

    // registrar producto (lo usa el admin)
    public boolean registrar(String nombre, int cantidad, double valor) {
        return registrar(nombre, cantidad, valor, "General");
    }

    public boolean registrar(String nombre, int cantidad, double valor, String categoria) {
        if (nombre == null || nombre.trim().isEmpty() || cantidad < 0 || valor <= 0) {
            return false;
        }
        if (buscarPorNombre(nombre) != null) {
            return false; // ya existe
        }
        productos.add(new producto(nombre.trim(), cantidad, valor, categoria));
        GestorJSON.guardarProductos(productos);
        return true;
    }

    // Buscar por nombre (ignora mayúsculas)
    public producto buscarPorNombre(String nombre) {
        if (nombre == null) {
            return null;
        }
        for (producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre.trim())) {
                return p;
            }
        }
        return null;
    }

    // Buscar productos que contengan un texto (barra de búsqueda del catálogo)
    public ArrayList<producto> buscar(String texto) {
        ArrayList<producto> resultado = new ArrayList<>();
        if (texto == null) {
            return resultado;
        }
        for (producto p : productos) {
            if (p.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // Editar producto por nombre
    public boolean editar(String nombre, int nuevaCantidad, double nuevoValor) {
        producto p = buscarPorNombre(nombre);
        if (p == null) {
            return false;
        }
        p.setCantidad(nuevaCantidad);
        p.setValor(nuevoValor);
        GestorJSON.guardarProductos(productos);
        return true;
    }

    // Editar un producto ya seleccionado (permite cambiar el nombre)
    public boolean actualizar(producto original, String nombre, int cantidad, double valor, String categoria) {
        if (original == null || nombre == null || nombre.trim().isEmpty() || cantidad < 0 || valor <= 0) {
            return false;
        }
        producto existente = buscarPorNombre(nombre);
        if (existente != null && existente != original) {
            return false; // el nombre ya lo usa otro producto
        }
        original.setNombre(nombre.trim());
        original.setCantidad(cantidad);
        original.setValor(valor);
        original.setCategoria(categoria);
        GestorJSON.guardarProductos(productos);
        return true;
    }

    // Eliminar producto
    public boolean eliminar(String nombre) {
        producto p = buscarPorNombre(nombre);
        if (p == null) {
            return false;
        }
        boolean eliminado = productos.remove(p);
        if (eliminado) {
            GestorJSON.guardarProductos(productos);
        }
        return eliminado;
    }

    // Descontar stock al comprar
    public boolean descontarStock(String nombre, int unidades) {
        producto p = buscarPorNombre(nombre);
        if (p == null || unidades <= 0 || p.getCantidad() < unidades) {
            return false;
        }
        p.setCantidad(p.getCantidad() - unidades);
        GestorJSON.guardarProductos(productos);
        return true;
    }

    public ArrayList<producto> getProductos() {
        return productos;
    }

    public void listar() {
        if (productos.isEmpty()) {
            System.out.println("El catálogo está vacío.");
            return;
        }
        for (int i = 0; i < productos.size(); i++) {
            System.out.println((i + 1) + ". " + productos.get(i));
        }
    }

}
