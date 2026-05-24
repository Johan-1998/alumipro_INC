package com.alumipro.mobile.model;

import java.util.ArrayList;
import java.util.List;

public class VentaCreateRequest {
    private int clienteId;
    private List<ItemVentaRequest> items = new ArrayList<>();

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public List<ItemVentaRequest> getItems() { return items; }
    public void setItems(List<ItemVentaRequest> items) { this.items = items; }

    public static class ItemVentaRequest {
        private int productoId;
        private int cantidad;

        public ItemVentaRequest(int productoId, int cantidad) {
            this.productoId = productoId;
            this.cantidad = cantidad;
        }

        public int getProductoId() { return productoId; }
        public void setProductoId(int productoId) { this.productoId = productoId; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }
}
