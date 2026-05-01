/* archivo que contiene la lógica de venta */
package com.alumipro.service;

import com.alumipro.domain.dto.ItemVentaForm;
import com.alumipro.domain.dto.VentaForm;
import com.alumipro.domain.entity.*;
import com.alumipro.repository.ClienteRepository;
import com.alumipro.repository.DetalleVentaRepository;
import com.alumipro.repository.ProductoRepository;
import com.alumipro.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;

    public VentaService(
            VentaRepository ventaRepository,
            DetalleVentaRepository detalleVentaRepository,
            ProductoRepository productoRepository,
            ClienteRepository clienteRepository
    ) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Integer registrarVenta(VentaForm form, Usuario vendedor) {
        // ── Validaciones de entrada ─────────────────────────────────────────
        if (form.getClienteId() == null) {
            throw new IllegalArgumentException("Debe seleccionar un cliente.");
        }
        if (form.getItems() == null || form.getItems().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar al menos un producto.");
        }

        Cliente cliente = clienteRepository.findById(form.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + form.getClienteId()));

        // ── Crear cabecera de venta ─────────────────────────────────────────
        Venta venta = new Venta();
        venta.setFecha(LocalDate.now());
        venta.setCliente(cliente);
        venta.setVendedor(vendedor);
        venta.setTotal(BigDecimal.ZERO);
        venta = ventaRepository.save(venta);
        ventaRepository.flush(); // garantiza que el ID está asignado antes de usarlo en detalles

        // ── Procesar ítems ─────────────────────────────────────────────────
        BigDecimal total = BigDecimal.ZERO;
        List<DetalleVenta> detalles = new ArrayList<>();

        for (ItemVentaForm item : form.getItems()) {
            if (item == null) continue;

            if (item.getProductoId() == null) {
                throw new IllegalArgumentException("Producto es obligatorio en cada ítem.");
            }
            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new IllegalArgumentException("Cantidad debe ser mayor a 0.");
            }

            /*
             * FIX: obtener el producto ANTES del descontarStock para tener precio y nombre.
             * LUEGO hacer el UPDATE nativo. Como ProductoRepository.descontarStockSiDisponible()
             * ya tiene clearAutomatically=true, el EntityManager limpia la caché del Producto
             * después del UPDATE, eliminando el riesgo de que Hibernate sobreescriba el stock.
             */
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + item.getProductoId()));

            int actualizado = productoRepository.descontarStockSiDisponible(producto.getId(), item.getCantidad());
            if (actualizado == 0) {
                throw new IllegalArgumentException("Stock insuficiente para: " + producto.getNombre()
                        + " (solicitado: " + item.getCantidad() + ")");
            }

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));

            DetalleVenta dv = new DetalleVenta();
            dv.setVenta(venta);
            dv.setProducto(producto);
            dv.setCantidad(item.getCantidad());
            dv.setSubtotal(subtotal);
            detalles.add(dv);

            total = total.add(subtotal);
        }

        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La venta debe tener al menos un producto válido.");
        }

        // ── Guardar detalles y actualizar total ────────────────────────────
        detalleVentaRepository.saveAll(detalles); // más eficiente que save() uno a uno
        venta.setTotal(total);
        ventaRepository.save(venta);

        return venta.getId();
    }
}
