/* archivo que expone la api de venta */
package com.alumipro.web.api;

import com.alumipro.domain.dto.VentaForm;
import com.alumipro.domain.entity.DetalleVenta;
import com.alumipro.domain.entity.Usuario;
import com.alumipro.domain.entity.Venta;
import com.alumipro.repository.DetalleVentaRepository;
import com.alumipro.repository.UsuarioRepository;
import com.alumipro.repository.VentaRepository;
import com.alumipro.service.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ventas")
public class ApiVentaController {

    private final VentaService ventaService;
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final UsuarioRepository usuarioRepository;

    public ApiVentaController(
            VentaService ventaService,
            VentaRepository ventaRepository,
            DetalleVentaRepository detalleVentaRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.ventaService = ventaService;
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public static class VentaCreateResponse {
        public Integer ventaId;
        public BigDecimal total;

        public VentaCreateResponse(Integer ventaId, BigDecimal total) {
            this.ventaId = ventaId;
            this.total = total;
        }
    }

    public static class VentaSummary {
        public Integer id;
        public LocalDate fecha;
        public Integer clienteId;
        public String clienteNombre;
        public Integer vendedorId;
        public String vendedorNombre;
        public BigDecimal total;

        public VentaSummary(Integer id, LocalDate fecha, Integer clienteId, String clienteNombre, Integer vendedorId, String vendedorNombre, BigDecimal total) {
            this.id = id;
            this.fecha = fecha;
            this.clienteId = clienteId;
            this.clienteNombre = clienteNombre;
            this.vendedorId = vendedorId;
            this.vendedorNombre = vendedorNombre;
            this.total = total;
        }
    }

    public static class VentaDetalleItem {
        public Integer productoId;
        public String productoNombre;
        public BigDecimal precioUnitario;
        public Integer cantidad;
        public BigDecimal subtotal;

        public VentaDetalleItem(Integer productoId, String productoNombre, BigDecimal precioUnitario, Integer cantidad, BigDecimal subtotal) {
            this.productoId = productoId;
            this.productoNombre = productoNombre;
            this.precioUnitario = precioUnitario;
            this.cantidad = cantidad;
            this.subtotal = subtotal;
        }
    }

    public static class VentaDetalle {
        public Integer id;
        public LocalDate fecha;
        public Integer clienteId;
        public String clienteNombre;
        public Integer vendedorId;
        public String vendedorNombre;
        public BigDecimal total;
        public List<VentaDetalleItem> items;

        public VentaDetalle(Integer id, LocalDate fecha, Integer clienteId, String clienteNombre, Integer vendedorId, String vendedorNombre, BigDecimal total, List<VentaDetalleItem> items) {
            this.id = id;
            this.fecha = fecha;
            this.clienteId = clienteId;
            this.clienteNombre = clienteNombre;
            this.vendedorId = vendedorId;
            this.vendedorNombre = vendedorNombre;
            this.total = total;
            this.items = items;
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VentaCreateResponse crear(@RequestBody VentaForm form, Authentication auth) {
        if (auth == null || auth.getName() == null) {
            throw new IllegalArgumentException("Debe iniciar sesión.");
        }

        Usuario vendedor = usuarioRepository.findByCorreo(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        Integer ventaId = ventaService.registrarVenta(form, vendedor);
        Venta v = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new IllegalStateException("Venta no encontrada: " + ventaId));
        return new VentaCreateResponse(v.getId(), v.getTotal());
    }

    @GetMapping
    public List<VentaSummary> listar() {
        return ventaRepository.findAllWithClienteYVendedor().stream()
                .map(v -> new VentaSummary(
                        v.getId(),
                        v.getFecha(),
                        v.getCliente().getId(),
                        v.getCliente().getNombre(),
                        v.getVendedor() != null ? v.getVendedor().getId() : null,
                        v.getVendedor() != null ? v.getVendedor().getNombre() : "",
                        v.getTotal()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VentaDetalle detalle(@PathVariable Integer id) {
        Venta v = ventaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada."));

        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaIdWithProducto(id);
        List<VentaDetalleItem> items = detalles.stream()
                .map(d -> new VentaDetalleItem(
                        d.getProducto().getId(),
                        d.getProducto().getNombre(),
                        d.getProducto().getPrecio(),
                        d.getCantidad(),
                        d.getSubtotal()
                ))
                .collect(Collectors.toList());

        return new VentaDetalle(
                v.getId(),
                v.getFecha(),
                v.getCliente().getId(),
                v.getCliente().getNombre(),
                v.getVendedor() != null ? v.getVendedor().getId() : null,
                v.getVendedor() != null ? v.getVendedor().getNombre() : "",
                v.getTotal(),
                items
        );
    }
}
