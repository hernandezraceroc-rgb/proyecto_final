/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usuarios;

/**
 *
 * @author herna
 */
public class usuario {
    
    public enum Rol {
        CLIENTE, ADMIN
    }
 
    private String nombreUsuario;
    private String clave;
    private Rol rol;
 
    public usuario(String nombreUsuario, String clave, Rol rol) {
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
        this.rol = rol;
    }
 
    public String getNombreUsuario() {
        return nombreUsuario;
    }
 
    public String getClave() {
        return clave;
    }
 
    public Rol getRol() {
        return rol;
    }
 
    public boolean esAdmin() {
        return rol == Rol.ADMIN;
    }
 
    @Override
    public String toString() {
        return nombreUsuario + " (" + rol + ")";
    }
}
