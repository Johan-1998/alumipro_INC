/*
 * archivo: ApiMovilController
 * CREADO: endpoint /api/movil/notificaciones faltaba en el proyecto original.
 */
package com.alumipro.web.api;

import com.alumipro.domain.entity.Venta;
import com.alumipro.model.NotificacionMovil;
import com.alumipro.repository.VentaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movil")
public class ApiMovilController {

    private final VentaRepository ventaRepository;

    public ApiMovilController(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    /**
     * Retorna las últimas 20 ventas como notificaciones operativas.
     * Endpoint consumido por AyudaFragment en la app Android.
     */
    @GetMapping("/notificaciones")
    public List<NotificacionMovil> listarNotificaciones() {
        return ventaRepository.findAllWithClienteYVendedor().stream()
                .limit(20)
                .map(v -> {
                    NotificacionMovil n = new NotificacionMovil();
                    n.setId(v.getId() != null ? v.getId().longValue() : 0L);
                    n.setTitulo("Venta registrada");
                    n.setMensaje("Cliente: " + v.getCliente().getNombre()
                            + " · Total: $" + String.format("%,.0f", v.getTotal()));
                    n.setTipo("VENTA");
                    n.setCreatedAt(v.getFecha() != null ? v.getFecha().toString() : "");
                    return n;
                })
                .collect(Collectors.toList());
    }
}
