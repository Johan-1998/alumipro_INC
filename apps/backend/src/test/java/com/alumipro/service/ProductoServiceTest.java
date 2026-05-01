package com.alumipro.service;

import com.alumipro.domain.entity.Producto;
import com.alumipro.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void crearDebeGuardarProductoValido() {
        Producto producto = new Producto();
        producto.setNombre("Lámina aluminio");
        producto.setPrecio(new BigDecimal("250000"));
        producto.setStock(10);
        producto.setDescripcion("Producto para fachada");

        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> {
            Producto saved = inv.getArgument(0);
            saved.setId(50);
            return saved;
        });

        Producto saved = productoService.crear(producto);

        assertEquals(50, saved.getId());
        assertEquals("Lámina aluminio", saved.getNombre());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void crearDebeRechazarPrecioNegativo() {
        Producto producto = new Producto();
        producto.setNombre("Lámina aluminio");
        producto.setPrecio(new BigDecimal("-1"));
        producto.setStock(10);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> productoService.crear(producto));

        assertEquals("El precio debe ser >= 0.", ex.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void crearDebeRechazarStockNegativo() {
        Producto producto = new Producto();
        producto.setNombre("Lámina aluminio");
        producto.setPrecio(new BigDecimal("100"));
        producto.setStock(-1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> productoService.crear(producto));

        assertEquals("El stock debe ser >= 0.", ex.getMessage());
        verify(productoRepository, never()).save(any());
    }
}
