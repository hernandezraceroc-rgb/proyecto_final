# Diseño del prototipo (Figma)

Mockup clicable de la tienda virtual: https://www.figma.com/design/0kFDins6EvNkfGdgSAMzXP

El flujo empieza en la pantalla de Login (modo Present de Figma).

## Pantallas

| # | Pantalla | Qué muestra | Estructura / archivo |
|---|----------|-------------|----------------------|
| 1 | Login | Correo, contraseña y selector Cliente / Administrador | usuarios.json |
| 2 | Catálogo | Tarjetas con foto, nombre, precio, stock, "Agregar al carrito" y "+ Deseos" | ArrayList<Producto> ← productos.json |
| 3 | Carrito | Ítems, cantidades, quitar, resumen con total, "Finalizar compra", "Deshacer" | ArrayList<ItemCarrito> + Stack para deshacer |
| 4 | Historial | Compras pasadas; la más reciente arriba | Stack<Compra> → historial.json |
| 5 | Lista de deseos | Productos guardados, pasar al carrito o quitar | ArrayList<Producto> → deseos.json |
| 6 | Admin – Productos | Formulario (nombre, cantidad, valor) y tabla de inventario con editar/eliminar | ArrayList.add() + productos.json |
| 7 | Admin – Pedidos | Cola de pedidos pendientes; se procesa el primero | Queue<Pedido> (FIFO) → pedidos.json |
| 8 | Estructuras y archivos | Resumen de qué estructura usa cada módulo | — |

## Estructuras de datos

- **ArrayList**: catálogo, carrito, lista de deseos y usuarios (add, remove, get, size).
- **Stack (pila, LIFO)**: historial (la última compra queda en el tope) y deshacer en el carrito (push, pop, peek).
- **Queue (cola, FIFO)**: pedidos pendientes; el administrador atiende primero el más antiguo (enqueue, dequeue, peek).
- **Archivos JSON**: se leen al iniciar y se escriben al cambiar algo. Ejemplo:

```json
[
  { "id": 1, "nombre": "Audífonos Bluetooth Pro", "cantidad": 24, "valor": 129900 },
  { "id": 2, "nombre": "Teclado Mecánico RGB", "cantidad": 12, "valor": 219900 }
]
```

## Flujo general

1. El administrador registra un producto,  se agrega al ArrayList y se guarda en productos.json.
2. El cliente inicia sesión y ve el catálogo con ese producto.
3. Agrega al carrito (cada acción se apila para poder deshacer) o a deseos.
4. Finaliza la compra, se hace push al historial y enqueue del pedido.
5. El administrador procesa el pedido (dequeue) y se descuenta el stock en productos.json.
