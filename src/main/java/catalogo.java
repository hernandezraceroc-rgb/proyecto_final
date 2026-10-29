
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author herna
 */
public class catalogo {
    
    private ArrayList <producto> productos;
    
    public catalogo(){
        
        productos = new ArrayList<>();
        
    }
    
    //registrar producto(lo usa el admin)

  public boolean registrar(String nombre, int cantidad, double valor) {
        if (nombre == null || nombre.trim().isEmpty() || cantidad < 0 || valor <= 0) {
            return false;
    }
    if (buscarPorNombre(nombre) != null) {
            return false; // ya existe
    }
    productos.add(new producto(nombre.trim(), cantidad, valor));
        return true;
    }
    // Buscar por nombre (ignora mayúsculas)
    public producto buscarPorNombre(String nombre) {
        for (producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre.trim())) {
                return p;
            }
        }
        return null;
    }
        
         // Buscar productos que contengan un texto (barra de búsqueda del catálogo)
         public ArrayList<producto> buscar(String texto){
        ArrayList<producto> resultado = new ArrayList<>();
        for (producto p : productos) {
            if (p.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(p);
            }
        }
        return resultado;
    }
     // Editar producto
    public boolean editar(String nombre, int nuevaCantidad, double nuevoValor) {
        producto p = buscarPorNombre(nombre);
        if (p == null) {
            return false;
        }
        p.setCantidad(nuevaCantidad);
        p.setValor(nuevoValor);
        return true;
    }
    
      // Eliminar producto
    public boolean eliminar(String nombre) {
        producto p = buscarPorNombre(nombre);
        if (p == null) {
            return false;
        }
        return productos.remove(p);
    }
    
    // Descontar stock al comprar
    public boolean descontarStock(String nombre, int unidades) {
        producto p = buscarPorNombre(nombre);
        if (p == null || unidades <= 0 || p.getCantidad() < unidades) {
            return false;
        }
        p.setCantidad(p.getCantidad() - unidades);
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
