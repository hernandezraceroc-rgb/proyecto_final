/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usuarios;

import java.util.ArrayList;

/**
 *
 * @author herna
 */
public class gestorusuarios {
    
    private ArrayList<usuario> usuarios;
 
    public gestorusuarios() {
        usuarios = new ArrayList<>();
        // Admin precargado para poder registrar productos
        usuarios.add(new usuario("admin", "admin123", usuario.Rol.ADMIN));
    }
    // Registro de un cliente nuevo
    public boolean registrarCliente(String nombreUsuario, String clave) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()
                || clave == null || clave.length() < 4) {
            return false;
        }
        if (buscar(nombreUsuario) != null) {
            return false; // el usuario ya existe
        }
        usuarios.add(new usuario(nombreUsuario.trim(), clave, usuario.Rol.CLIENTE));
        return true;
    }
    // Login: devuelve el usuario si las credenciales son correctas, o null si no
    public usuario login(String nombreUsuario, String clave) {
        usuario u = buscar(nombreUsuario);
        if (u != null && u.getClave().equals(clave)) {
            return u;
        }
        return null;
    }
    // Buscar usuario por nombre (ignora mayúsculas)
    public usuario buscar(String nombreUsuario) {
        if (nombreUsuario == null) {
            return null;
        }
        for (usuario u : usuarios) {
            if (u.getNombreUsuario().equalsIgnoreCase(nombreUsuario.trim())) {
                return u;
            }
        }
        return null;
    }
 
    public ArrayList<usuario> getUsuarios() {
        return usuarios;
    }
}
