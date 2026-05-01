/* archivo que maneja el repositorio de detalleventa */
package com.alumipro.repository;

import com.alumipro.domain.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {

    @Query("select d from DetalleVenta d join fetch d.producto p join fetch d.venta v where v.id = :ventaId")
    List<DetalleVenta> findByVentaIdWithProducto(@Param("ventaId") Integer ventaId);
}
