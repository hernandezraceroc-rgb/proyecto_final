package modelo;

import java.util.ArrayList;

public class Pedido {

    private int id;
    private String fecha;
    private String cliente;
    private double total;
    private ArrayList<ItemCompra> items;

    public Pedido() {
        this.items = new ArrayList<>();
    }

    public Pedido(int id, String fecha, String cliente, ArrayList<ItemCompra> items, double total) {
        this.id = id;
        this.fecha = fecha;
        this.cliente = cliente;
        this.items = items == null ? new ArrayList<>() : items;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList<ItemCompra> getItems() {
        return items == null ? new ArrayList<>() : items;
    }

    public void setItems(ArrayList<ItemCompra> items) {
        this.items = items;
    }

    public String getResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido #").append(id).append(" - ").append(cliente).append("\n");
        sb.append(fecha).append("\n\n");
        for (ItemCompra item : getItems()) {
            sb.append("• ").append(item).append("\n");
        }
        sb.append("\nTotal: $ ").append(String.format("%,.0f", total));
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " - " + cliente + " - $ " + String.format("%,.0f", total);
    }
}
