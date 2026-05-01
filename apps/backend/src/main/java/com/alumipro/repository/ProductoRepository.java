package com.alumipro.repository;

import com.alumipro.domain.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    /*
     * FIX: clearAutomatically = true — Spring Boot 2.7 compatible.
     * flushBeforeExecution no existe en Spring Data JPA < 2.5.8 / Boot < 2.7.x
     * El flush manual se hace en VentaService antes de llamar este método.
     * clearAutomatically invalida el L1 cache después del UPDATE nativo,
     * evitando que Hibernate sobreescriba el stock con el valor viejo al hacer flush.
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Producto p SET p.stock = p.stock - :cantidad " +
           "WHERE p.id = :id AND p.stock >= :cantidad")
    int descontarStockSiDisponible(@Param("id") Integer id, @Param("cantidad") Integer cantidad);
}
