/* archivo que controla venta */
package com.alumipro.web.controller;

import com.alumipro.domain.dto.VentaForm;
import com.alumipro.domain.entity.Usuario;
import com.alumipro.repository.ClienteRepository;
import com.alumipro.repository.ProductoRepository;
import com.alumipro.repository.UsuarioRepository;
import com.alumipro.service.VentaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final VentaService ventaService;
    private final UsuarioRepository usuarioRepository;

    public VentaController(ClienteRepository clienteRepository,
                           ProductoRepository productoRepository,
                           VentaService ventaService,
                           UsuarioRepository usuarioRepository) {
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.ventaService = ventaService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/nueva")
    public String formNueva(Model model) {


        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("ventaForm", new VentaForm());
        return "ventas/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("ventaForm") VentaForm ventaForm, Model model, Authentication auth) {
        try {
            if (auth == null || auth.getName() == null) {
                throw new IllegalArgumentException("Debe iniciar sesión.");
            }

            Usuario vendedor = usuarioRepository.findByCorreo(auth.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

            Integer ventaId = ventaService.registrarVenta(ventaForm, vendedor);
            model.addAttribute("ventaId", ventaId);
            return "ventas/resultado";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("clientes", clienteRepository.findAll());
            model.addAttribute("productos", productoRepository.findAll());
            model.addAttribute("error", ex.getMessage());
            return "ventas/form";
        }
    }
}
