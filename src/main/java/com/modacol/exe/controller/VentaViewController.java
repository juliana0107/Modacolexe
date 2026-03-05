package com.modacol.exe.controller;

import com.modacol.exe.dto.DetalleVentaDTO;
import com.modacol.exe.dto.VentaDTO;
import com.modacol.exe.service.ClienteService;
import com.modacol.exe.service.ProductoService;
import com.modacol.exe.service.UsuarioService;
import com.modacol.exe.service.VentaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaViewController {

    private final VentaService ventaService;
    private final UsuarioService usuarioService;
    private final ClienteService clienteService;
    private final ProductoService productoService;

    public VentaViewController(VentaService ventaService,
                               UsuarioService usuarioService,
                               ClienteService clienteService,
                               ProductoService productoService) {
        this.ventaService = ventaService;
        this.usuarioService = usuarioService;
        this.clienteService = clienteService;
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Long clienteId,
            Model model) {

        List<VentaDTO> ventas;

        // Si cualquiera de los filtros tiene valor, usamos el método filtrar
        if (fechaInicio != null || fechaFin != null || clienteId != null) {
            ventas = ventaService.filtrar(fechaInicio, fechaFin, clienteId);
        } else {
            ventas = ventaService.listar();
        }

        model.addAttribute("ventas", ventas);
        model.addAttribute("clientes", clienteService.listar());

        // Mantener los valores en los inputs del filtro después de buscar
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("clienteIdSeleccionado", clienteId);

        return "ventas/list";
    }

    @GetMapping("/new")
    public String crear(Model model) {
        VentaDTO dto = new VentaDTO();
        dto.setDetalles(new ArrayList<>());

        // Inicializamos con 1 fila vacía para que el JS no falle al clonar
        dto.getDetalles().add(new DetalleVentaDTO());

        model.addAttribute("venta", dto);
        model.addAttribute("usuarios", usuarioService.listar());
        model.addAttribute("clientes", clienteService.listar());
        model.addAttribute("productos", productoService.listar());

        return "ventas/form";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) {
        VentaDTO dto = ventaService.obtenerPorId(id);

        model.addAttribute("venta", dto);
        model.addAttribute("usuarios", usuarioService.listar());
        model.addAttribute("clientes", clienteService.listar());
        model.addAttribute("productos", productoService.listar());

        return "ventas/form";
    }

    @PostMapping
    public String guardar(@ModelAttribute("venta") VentaDTO ventaDto, Model model) {
        try {
            if (ventaDto.getId() == null) {
                ventaService.crear(ventaDto);
            } else {
                ventaService.actualizar(ventaDto.getId(), ventaDto);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar la venta: " + e.getMessage());
            model.addAttribute("usuarios", usuarioService.listar());
            model.addAttribute("clientes", clienteService.listar());
            model.addAttribute("productos", productoService.listar());
            return "ventas/form";
        }
        return "redirect:/ventas";
    }

    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id) {
        ventaService.eliminar(id);
        return "redirect:/ventas";
    }
}