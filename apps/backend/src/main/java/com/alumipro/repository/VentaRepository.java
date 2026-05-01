/* archivo que maneja el repositorio de venta */
package com.alumipro.repository;

import com.alumipro.domain.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

    @Query("select v from Venta v join fetch v.cliente c left join fetch v.vendedor u order by v.fecha desc, v.id desc")
    List<Venta> findAllWithClienteYVendedor();
}
