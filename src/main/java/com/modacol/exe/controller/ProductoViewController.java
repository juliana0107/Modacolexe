package com.modacol.exe.controller;

import com.modacol.exe.dto.ProductoDTO;
import com.modacol.exe.service.ProductoService;
import com.modacol.exe.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoViewController {

    private final ProductoService productoService;
    private final ProveedorService proveedorService;

    public ProductoViewController(ProductoService productoService, ProveedorService proveedorService) {
        this.productoService = productoService;
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String nombre,
                        @RequestParam(required = false) String descripcion,
                        @RequestParam(required = false) Long proveedorId,
                        Model model) {
        List<ProductoDTO> productos;
        if (nombre != null || descripcion != null || proveedorId != null) {
            productos = productoService.filtrar(nombre, descripcion, proveedorId);
        } else {
            productos = productoService.listar();
        }
        model.addAttribute("productos", productos);
        model.addAttribute("proveedores", proveedorService.listar());
        model.addAttribute("content", "productos/list :: content");
        model.addAttribute("pageTitle", "Productos - Modacol");
        return "productos/list";
    }

    @GetMapping("/new")
    public String crear(Model model) {
        model.addAttribute("producto", new ProductoDTO());
        model.addAttribute("proveedores", proveedorService.listar());
        model.addAttribute("content", "productos/form :: content");
        model.addAttribute("pageTitle", "Nuevo Producto - Modacol");
        return "productos/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoService.buscarPorId(id));
        model.addAttribute("productoId", id);
        model.addAttribute("proveedores", proveedorService.listar());
        model.addAttribute("content", "productos/form :: content");
        model.addAttribute("pageTitle", "Editar Producto - Modacol");
        return "productos/form";
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute("producto") ProductoDTO productoDTO, 
                         BindingResult result, 
                         @RequestParam(value = "id", required = false) Long id,
                         Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("proveedores", proveedorService.listar());
            model.addAttribute("content", "productos/form :: content");
            model.addAttribute("pageTitle", productoDTO.getId() == null ? "Nuevo Producto - Modacol" : "Editar Producto - Modacol");
            return "productos/form";
        }
        
        try {
            if (productoDTO.getId() == null) {
                productoService.crear(productoDTO);
            } else {
                productoService.actualizar(productoDTO.getId(), productoDTO);
            }
        } catch (Exception e) {
            result.reject("error.general", "Error al guardar el producto: " + e.getMessage());
            model.addAttribute("proveedores", proveedorService.listar());
            model.addAttribute("content", "productos/form :: content");
            model.addAttribute("pageTitle", productoDTO.getId() == null ? "Nuevo Producto - Modacol" : "Editar Producto - Modacol");
            return "productos/form";
        }

        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return "redirect:/productos";
    }
}
