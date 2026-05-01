/* archivo que controla producto */
package com.alumipro.web.controller;

import com.alumipro.domain.entity.Producto;
import com.alumipro.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listar());
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto, Model model) {
        try {
            productoService.crear(producto);
            return "redirect:/productos";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("producto", producto);
            model.addAttribute("error", ex.getMessage());
            return "productos/form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("producto", productoService.obtenerPorId(id));
        return "productos/form_edit";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id, @ModelAttribute Producto producto, Model model) {
        try {
            productoService.actualizar(id, producto);
            return "redirect:/productos";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("producto", producto);
            model.addAttribute("error", ex.getMessage());
            return "productos/form_edit";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return "redirect:/productos";
    }
}
