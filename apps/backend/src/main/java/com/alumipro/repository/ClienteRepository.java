/* archivo que maneja el repositorio de cliente */
package com.alumipro.repository;

import com.alumipro.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
