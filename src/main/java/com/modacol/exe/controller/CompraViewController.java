package com.modacol.exe.controller;

import com.modacol.exe.dto.CompraDTO;
import com.modacol.exe.service.CompraService;
import com.modacol.exe.service.UsuarioService;
import com.modacol.exe.service.ProveedorService;
import com.modacol.exe.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/compras")
public class CompraViewController {

    private final CompraService compraService;
    private final UsuarioService usuarioService;
    private final ProveedorService proveedorService;
    private final ProductoService productoService;

    public CompraViewController(CompraService compraService,
                                UsuarioService usuarioService,
                                ProveedorService proveedorService,
                                ProductoService productoService) {
        this.compraService = compraService;
        this.usuarioService = usuarioService;
        this.proveedorService = proveedorService;
        this.productoService = productoService;
    }

    // LISTADO DE COMPRAS
    @GetMapping
    public String listarCompras(
            @RequestParam(required = false) LocalDate fechaCompra,
            @RequestParam(required = false) LocalDate fechaEstimadaEntrega,
            @RequestParam(required = false) Long proveedorId,
            Model model) {

        // Ejecutar el filtro (Asegúrate de haber implementado esto en el Service como vimos antes)
        List<CompraDTO> compras = compraService.filtrar(fechaCompra, fechaEstimadaEntrega, proveedorId);

        model.addAttribute("comprasList", compras);
        model.addAttribute("proveedores", proveedorService.listar());

        // OJO: Cambié "compras/list" por "compras/index" para que coincida con tu archivo
        return "compras/list";
    }

    // FORMULARIO NUEVA COMPRA
    @GetMapping("/new")
    public String crear(Model model) {
        CompraDTO compra = compraService.crearCompraVacia();
        model.addAttribute("compra", compra);
        cargarCombos(model);
        return "compras/form";
    }

    // FORMULARIO EDITAR COMPRA
    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) {
        CompraDTO compra = compraService.obtenerPorId(id);
        if (compra == null) {
            return "redirect:/compras?noEncontrada";
        }

        model.addAttribute("compra", compra);
        cargarCombos(model);
        return "compras/form";
    }

    // GUARDAR
    @PostMapping
    public String guardar(
            @Valid @ModelAttribute("compra") CompraDTO formDTO,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            cargarCombos(model);
            return "compras/form";
        }

        if (formDTO.getFechaEstimadaEntrega() != null &&
                formDTO.getFechaCompra() != null &&
                formDTO.getFechaEstimadaEntrega().isBefore(formDTO.getFechaCompra())) {

            result.rejectValue("fechaEstimadaEntrega", "fecha.invalida",
                    "La fecha de entrega no puede ser anterior a la fecha de compra");
            cargarCombos(model);
            return "compras/form";
        }

        if (formDTO.getId() == null) {
            compraService.crear(formDTO);
        } else {
            compraService.actualizar(formDTO.getId(), formDTO);
        }

        return "redirect:/compras?ok";
    }

    // ELIMINAR
    @PostMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id) {
        compraService.eliminar(id);
        return "redirect:/compras?eliminada";
    }

    private void cargarCombos(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        model.addAttribute("proveedores", proveedorService.listar());
        model.addAttribute("tiposDocumento", compraService.obtenerTiposDocumento());
        model.addAttribute("formasPago", compraService.obtenerFormasPago());
        model.addAttribute("estados", compraService.obtenerEstadosCompra());
        model.addAttribute("productos", productoService.listar());
    }
}