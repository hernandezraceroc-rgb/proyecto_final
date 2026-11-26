package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Compra {

    private String fecha;
    private String cliente;
    private double total;
    private ArrayList<ItemCompra> items;

    public Compra() {
        this.items = new ArrayList<>();
    }

    public Compra(String cliente, ArrayList<ItemCompra> items, double total) {
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.cliente = cliente;
        this.items = items == null ? new ArrayList<>() : items;
        this.total = total;
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
        for (ItemCompra item : getItems()) {
            sb.append("• ").append(item).append("\n");
        }
        sb.append("\nTotal pagado: $ ").append(String.format("%,.0f", total));
        return sb.toString();
    }

    @Override
    public String toString() {
        return fecha + " - " + cliente + " - $ " + String.format("%,.0f", total);
    }
}
