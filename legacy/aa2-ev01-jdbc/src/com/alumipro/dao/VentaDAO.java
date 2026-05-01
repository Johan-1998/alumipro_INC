/* archivo java ventadao */
package com.alumipro.dao;

import com.alumipro.model.DetalleVenta;

import java.util.List;

public interface VentaDAO {


    int registrarVenta(int clienteId, List<DetalleVenta> detalles);
}
