package com.alumipro.service;

import com.alumipro.domain.dto.ItemVentaForm;
import com.alumipro.domain.dto.VentaForm;
import com.alumipro.domain.entity.*;
import com.alumipro.repository.ClienteRepository;
import com.alumipro.repository.DetalleVentaRepository;
import com.alumipro.repository.ProductoRepository;
import com.alumipro.repository.VentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private VentaService ventaService;

    @Test
    void registrarVentaDebeCalcularTotalYGuardarDetalle() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNombre("Carlos Pérez");

        Producto producto = new Producto();
        producto.setId(2);
        producto.setNombre("Aluminio perfil 2m");
        producto.setPrecio(new BigDecimal("98000"));
        producto.setStock(50);

        Usuario vendedor = new Usuario();
        vendedor.setId(10);
        vendedor.setNombre("Vendedor ALUMIPRO");
        vendedor.setRol(RolUsuario.VENDEDOR);

        VentaForm form = new VentaForm();
        form.setClienteId(1);
        ItemVentaForm item = new ItemVentaForm();
        item.setProductoId(2);
        item.setCantidad(2);
        form.setItems(List.of(item));

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(2)).thenReturn(Optional.of(producto));
        when(productoRepository.descontarStockSiDisponible(2, 2)).thenReturn(1);
        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> {
            Venta venta = inv.getArgument(0);
            if (venta.getId() == null) {
                venta.setId(77);
            }
            return venta;
        });

        Integer ventaId = ventaService.registrarVenta(form, vendedor);

        assertEquals(77, ventaId);
        verify(productoRepository).descontarStockSiDisponible(2, 2);
        verify(detalleVentaRepository).save(any(DetalleVenta.class));

        ArgumentCaptor<Venta> captor = ArgumentCaptor.forClass(Venta.class);
        verify(ventaRepository, times(2)).save(captor.capture());
        Venta ventaFinal = captor.getAllValues().get(1);
        assertEquals(0, new BigDecimal("196000").compareTo(ventaFinal.getTotal()));
        assertEquals(cliente, ventaFinal.getCliente());
        assertEquals(vendedor, ventaFinal.getVendedor());
    }

    @Test
    void registrarVentaDebeFallarCuandoNoHayStock() {
        Cliente cliente = new Cliente();
        cliente.setId(1);

        Producto producto = new Producto();
        producto.setId(2);
        producto.setNombre("Aluminio perfil 2m");
        producto.setPrecio(new BigDecimal("98000"));

        Usuario vendedor = new Usuario();
        vendedor.setId(10);

        VentaForm form = new VentaForm();
        form.setClienteId(1);
        ItemVentaForm item = new ItemVentaForm();
        item.setProductoId(2);
        item.setCantidad(99);
        form.setItems(List.of(item));

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(2)).thenReturn(Optional.of(producto));
        when(productoRepository.descontarStockSiDisponible(2, 99)).thenReturn(0);
        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> {
            Venta venta = inv.getArgument(0);
            if (venta.getId() == null) venta.setId(90);
            return venta;
        });

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ventaService.registrarVenta(form, vendedor));

        assertEquals("Stock insuficiente para: Aluminio perfil 2m", ex.getMessage());
        verify(detalleVentaRepository, never()).save(any());
    }
}
