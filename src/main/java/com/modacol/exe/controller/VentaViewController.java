package com.modacol.exe.controller;

import com.modacol.exe.dto.CompraDTO;
import com.modacol.exe.dto.DetalleVentaDTO;
import com.modacol.exe.dto.ProductoDTO;
import com.modacol.exe.dto.VentaDTO;
import com.modacol.exe.entity.DetalleVenta;
import com.modacol.exe.entity.Producto;
import com.modacol.exe.entity.Venta;
import com.modacol.exe.service.ClienteService;
import com.modacol.exe.service.ProductoService;
import com.modacol.exe.service.UsuarioService;
import com.modacol.exe.service.VentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public String listar(@RequestParam(required = false) String fechaInicio,
                        @RequestParam(required = false) String fechaFin,
                        @RequestParam(required = false) Long clienteId,
                        Model model) {
        List<VentaDTO> ventas;
        if (fechaInicio != null || fechaFin != null || clienteId != null) {
            java.time.LocalDate inicio = fechaInicio != null ? java.time.LocalDate.parse(fechaInicio) : null;
            java.time.LocalDate fin = fechaFin != null ? java.time.LocalDate.parse(fechaFin) : null;
            ventas = ventaService.filtrar(inicio, fin, clienteId);
        } else {
            ventas = ventaService.listar();
        }
        model.addAttribute("ventas", ventas);
        model.addAttribute("clientes", clienteService.listar());
        return "ventas/list";
    }

    @GetMapping("/new")
    public String crear(Model model) {
        VentaDTO dto = new VentaDTO();
        dto.setDetalles(new ArrayList<>());

        for (int i = 0; i < 3; i++) {
            dto.getDetalles().add(new DetalleVentaDTO());
        }

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
    public String guardar(@ModelAttribute("venta") VentaDTO ventaDto) {

        if (ventaDto.getId() == null) {
            ventaService.crear(ventaDto);
        } else {
            ventaService.actualizar(ventaDto.getId(), ventaDto);
        }

        return "redirect:/ventas";
    }


    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id) {
        ventaService.eliminar(id);
        return "redirect:/ventas";
    }
}
