package com.alumipro.mobile.model;

import java.util.List;

public class VentaDetalle {
    private int id;
    private String fecha;
    private int clienteId;
    private String clienteNombre;
    private Integer vendedorId;
    private String vendedorNombre;
    private double total;
    private List<VentaDetalleItem> items;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    public Integer getVendedorId() { return vendedorId; }
    public void setVendedorId(Integer vendedorId) { this.vendedorId = vendedorId; }
    public String getVendedorNombre() { return vendedorNombre; }
    public void setVendedorNombre(String vendedorNombre) { this.vendedorNombre = vendedorNombre; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public List<VentaDetalleItem> getItems() { return items; }
    public void setItems(List<VentaDetalleItem> items) { this.items = items; }
}
