package com.modacol.exe.controller;

import com.modacol.exe.dto.VentaDTO;
import com.modacol.exe.service.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // CAMBIADO: de @RestController a @Controller
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // <--- CLAVE: Cambiado para permitir redirecciones
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public String crear(@ModelAttribute VentaDTO dto) {
        ventaService.crear(dto);
        return "redirect:/ventas";
    }

    @PutMapping("/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute VentaDTO dto) {
        ventaService.actualizar(id, dto);
        return "redirect:/ventas";
    }

    // Para que estos métodos sigan devolviendo JSON (como antes),
    // agregamos @ResponseBody porque ahora la clase es @Controller
    @GetMapping
    @ResponseBody
    public List<VentaDTO> listar() {
        return ventaService.listar();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public VentaDTO obtener(@PathVariable Long id) {
        return ventaService.obtenerPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ventaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}