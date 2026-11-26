package persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import modelo.Compra;
import modelo.Pedido;
import modelo.producto;
import usuarios.usuario;

public class GestorJSON {

    private static final String CARPETA = "datos";
    private static final String RUTA_PRODUCTOS = CARPETA + "/productos.json";
    private static final String RUTA_USUARIOS = CARPETA + "/usuarios.json";
    private static final String RUTA_DESEOS = CARPETA + "/deseos.json";
    private static final String RUTA_HISTORIAL = CARPETA + "/historial.json";
    private static final String RUTA_PEDIDOS = CARPETA + "/pedidos.json";

    // setPrettyPrinting() hace que el JSON se guarde ordenado y legible
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static void asegurarCarpeta() {
        File dir = new File(CARPETA);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static void escribir(String ruta, Object datos) {
        asegurarCarpeta();
        try (Writer writer = Files.newBufferedWriter(Paths.get(ruta), StandardCharsets.UTF_8)) {
            gson.toJson(datos, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar " + ruta + ": " + e.getMessage());
        }
    }

    private static <T> ArrayList<T> leerLista(String ruta, Type tipo) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = Files.newBufferedReader(archivo.toPath(), StandardCharsets.UTF_8)) {
            ArrayList<T> lista = gson.fromJson(reader, tipo);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer " + ruta + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ==================== PRODUCTOS ====================

    public static void guardarProductos(ArrayList<producto> listaProductos) {
        escribir(RUTA_PRODUCTOS, listaProductos);
    }

    public static ArrayList<producto> cargarProductos() {
        return leerLista(RUTA_PRODUCTOS, new TypeToken<ArrayList<producto>>() {
        }.getType());
    }

    // ==================== USUARIOS ====================

    public static void guardarUsuarios(ArrayList<usuario> listaUsuarios) {
        escribir(RUTA_USUARIOS, listaUsuarios);
    }

    public static ArrayList<usuario> cargarUsuarios() {
        return leerLista(RUTA_USUARIOS, new TypeToken<ArrayList<usuario>>() {
        }.getType());
    }

    // ==================== LISTA DE DESEOS ====================

    public static void guardarDeseos(ArrayList<producto> listaDeseos) {
        escribir(RUTA_DESEOS, listaDeseos);
    }

    public static ArrayList<producto> cargarDeseos() {
        return leerLista(RUTA_DESEOS, new TypeToken<ArrayList<producto>>() {
        }.getType());
    }

    // ==================== HISTORIAL (pila) ====================

    public static void guardarHistorial(ArrayList<Compra> historial) {
        escribir(RUTA_HISTORIAL, historial);
    }

    public static ArrayList<Compra> cargarHistorial() {
        return leerLista(RUTA_HISTORIAL, new TypeToken<ArrayList<Compra>>() {
        }.getType());
    }

    // ==================== PEDIDOS (cola) ====================

    public static void guardarPedidos(ArrayList<Pedido> pedidos) {
        escribir(RUTA_PEDIDOS, pedidos);
    }

    public static ArrayList<Pedido> cargarPedidos() {
        return leerLista(RUTA_PEDIDOS, new TypeToken<ArrayList<Pedido>>() {
        }.getType());
    }
}
