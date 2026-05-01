/* archivo java itemventaform */
package com.alumipro.domain.dto;


public class ItemVentaForm {

    private Integer productoId;
    private Integer cantidad;

    public ItemVentaForm() {}

    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
