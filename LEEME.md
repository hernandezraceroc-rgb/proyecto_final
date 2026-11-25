# UniShop — login y catálogo funcionales

## Cómo ejecutar

En NetBeans: abre el proyecto y pulsa **Run** (usa `clean javafx:run`).
Por consola, desde la carpeta del proyecto:

```
mvn clean javafx:run
```

Requiere JDK 21 o superior (probado con JDK 23; JavaFX 21.0.6 se descarga desde Maven).

## Credenciales

| Modo | Correo | Contraseña |
|------|--------|------------|
| Cliente (demo) | `cliente@unishop.com` | `1234` |
| Administrador | `admin` | `admin123` |

También puedes crear un cliente desde **¿No tienes cuenta? Regístrate**.
El selector **Cliente / Administrador** debe coincidir con el tipo de usuario.

## Qué funciona

- **Login**: valida credenciales, valida el rol seleccionado, muestra errores y abre el catálogo.
- **Catálogo**: 16 productos de ejemplo, filtros por categoría, buscador por nombre/categoría.
- **Tarjetas**: imagen generada en código, precio, stock, *Agregar al carrito* y *+ Deseos*.
- **Carrito**: resumen, deshacer última acción, finalizar compra (descuenta stock y guarda historial).
- **Lista de deseos** e **historial**: pantallas propias con buscador; permite quitar deseos, pasarlos al carrito y consultar las compras realizadas.
- **Administrar** (solo visible para admin): pantalla para registrar, editar y eliminar productos.

## Estructura de paquetes

```text
vista/                 controladores FXML y App (fx:controller apunta aquí)
└── util/Recursos.java imágenes generadas en código
aplicacion/Sesion.java estado compartido (usuario, carrito, deseos, historial)
modelo/                producto, catalogo, Carrito, ListaDeseos, etc.
usuarios/              usuario y gestorusuarios
```

`module-info.java` solo abre `vista` a `javafx.fxml`; las demás clases están dentro del mismo módulo.

## Cambios principales respecto al ZIP original

- `App.java` ahora carga `login.fxml` y navega a `catalogo.fxml` (antes cargaba un `primary.fxml` inexistente).
- Los controladores de login, catálogo, carrito, deseos, historial y administración de productos están en `vista` y coinciden con `fx:controller`; los de pedidos siguen pendientes.
- Se implementaron todos los `onAction` y los `fx:id` faltantes (`btnAdmin` → `btnAdmin`, enlaces del header, etc.).
- Se quitó la imagen externa de `Downloads` que impedía abrir los FXML; el logo y las imágenes se generan en código.
- `pom.xml` usa JavaFX 21.0.6, Java 21 y `mainClass=vista.App`; `module-info.java` abre `vista` a `javafx.fxml`.
- `producto`/`catalogo` soportan categorías para los filtros; se añadió `Sesion.java` (en `aplicacion`) como estado compartido y `Recursos.java` (en `vista.util`) para las imágenes.
