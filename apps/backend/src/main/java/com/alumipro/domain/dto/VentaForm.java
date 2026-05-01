/* archivo java ventaform */
package com.alumipro.domain.dto;

import java.util.ArrayList;
import java.util.List;



public class VentaForm {

    private Integer clienteId;

    private List<ItemVentaForm> items = new ArrayList<>();

    public VentaForm() {
        items.add(new ItemVentaForm());
        items.add(new ItemVentaForm());
    }

    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }

    public List<ItemVentaForm> getItems() { return items; }
    public void setItems(List<ItemVentaForm> items) { this.items = items; }
}
