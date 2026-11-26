package persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import modelo.producto;
import usuarios.usuario;

public class GestorJSON {

    private static final String CARPETA = "datos";
    private static final String RUTA_PRODUCTOS = CARPETA + "/productos.json";
    private static final String RUTA_USUARIOS = CARPETA + "/usuarios.json";

    // setPrettyPrinting() hace que el JSON se guarde ordenado y legible, no en una sola línea
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static void asegurarCarpeta() {
        File dir = new File(CARPETA);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // ==================== PRODUCTOS ====================

    public static void guardarProductos(ArrayList<producto> listaProductos) {
        asegurarCarpeta();
        try (FileWriter writer = new FileWriter(RUTA_PRODUCTOS)) {
            gson.toJson(listaProductos, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar productos en JSON: " + e.getMessage());
        }
    }

    public static ArrayList<producto> cargarProductos() {
        File archivo = new File(RUTA_PRODUCTOS);
        if (!archivo.exists()) {
            return new ArrayList<>(); // Si aún no existe el archivo, devuelve lista vacía
        }
        try (FileReader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<ArrayList<producto>>() {}.getType();
            ArrayList<producto> lista = gson.fromJson(reader, tipoLista);
            return (lista != null) ? lista : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer productos desde JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ==================== USUARIOS ====================

    public static void guardarUsuarios(ArrayList<usuario> listaUsuarios) {
        asegurarCarpeta();
        try (FileWriter writer = new FileWriter(RUTA_USUARIOS)) {
            gson.toJson(listaUsuarios, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios en JSON: " + e.getMessage());
        }
    }

    public static ArrayList<usuario> cargarUsuarios() {
        File archivo = new File(RUTA_USUARIOS);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<ArrayList<usuario>>() {}.getType();
            ArrayList<usuario> lista = gson.fromJson(reader, tipoLista);
            return (lista != null) ? lista : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer usuarios desde JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}