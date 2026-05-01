/* archivo java venta */
package com.alumipro.model;

import java.time.LocalDate;

public class Venta {

    private int id;
    private LocalDate fecha;
    private int clienteId;
    private double total;

    public Venta() {
    }

    public Venta(int id, LocalDate fecha, int clienteId, double total) {
        this.id = id;
        this.fecha = fecha;
        this.clienteId = clienteId;
        this.total = total;
    }

    public Venta(LocalDate fecha, int clienteId, double total) {
        this.fecha = fecha;
        this.clienteId = clienteId;
        this.total = total;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
