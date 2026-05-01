/* archivo que contiene la lógica de producto */
package com.alumipro.service;

import com.alumipro.domain.entity.Producto;
import com.alumipro.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
    }

    public Producto crear(Producto producto) {
        validar(producto);
        producto.setId(null);
        return productoRepository.save(producto);
    }

    public Producto actualizar(Integer id, Producto producto) {
        validar(producto);

        Producto existente = obtenerPorId(id);
        existente.setNombre(producto.getNombre());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        existente.setDescripcion(producto.getDescripcion());

        return productoRepository.save(existente);
    }

    public void eliminar(Integer id) {
        Producto existente = obtenerPorId(id);
        productoRepository.delete(existente);
    }


    private void validar(Producto p) {
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (p.getPrecio() == null || p.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio debe ser >= 0.");
        }
        if (p.getStock() == null || p.getStock() < 0) {
            throw new IllegalArgumentException("El stock debe ser >= 0.");
        }

    }
}
