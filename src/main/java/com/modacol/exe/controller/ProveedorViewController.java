package com.modacol.exe.controller;

import com.modacol.exe.dto.ProveedorDTO;
import com.modacol.exe.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proveedores")
public class ProveedorViewController {

    private final ProveedorService proveedorService;

    public ProveedorViewController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String razonSocial,
                        @RequestParam(required = false) String identificacion,
                        @RequestParam(required = false) String correo,
                        Model model) {
        if (razonSocial != null || identificacion != null || correo != null) {
            model.addAttribute("proveedoresList", proveedorService.filtrar(razonSocial, identificacion, correo));
        } else {
            model.addAttribute("proveedoresList", proveedorService.listar());
        }
        return "proveedores/list";
    }

    @GetMapping("/new")
    public String crear(Model model) {
        model.addAttribute("proveedor", new ProveedorDTO());
        return "proveedores/form";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("proveedor", proveedorService.obtenerPorId(id));
        return "proveedores/form";
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute("proveedor") ProveedorDTO form, 
                         BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            return "proveedores/form";
        }
        
        try {
            ProveedorDTO dto = new ProveedorDTO();
            dto.setRazonSocial(form.getRazonSocial());
            dto.setIdentificacion(form.getIdentificacion());
            dto.setDireccion(form.getDireccion());
            dto.setCorreo(form.getCorreo());
            dto.setContacto(form.getContacto());

            if (form.getId() == null) {
                proveedorService.crear(dto);
            } else {
                proveedorService.actualizar(form.getId(), dto);
            }
        } catch (Exception e) {
            result.reject("error.general", "Error al guardar el proveedor: " + e.getMessage());
            return "proveedores/form";
        }

        return "redirect:/proveedores";
    }

    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id) {
        proveedorService.eliminar(id);
        return "redirect:/proveedores";
    }
}
