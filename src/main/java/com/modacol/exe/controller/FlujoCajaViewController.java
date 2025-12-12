package com.modacol.exe.controller;

import com.modacol.exe.entity.FlujoCaja;
import com.modacol.exe.service.FlujoCajaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller

public class FlujoCajaViewController {

    private final FlujoCajaService flujoCajaService;

    public FlujoCajaViewController(FlujoCajaService flujoCajaService) {
        this.flujoCajaService = flujoCajaService;
    }

    @GetMapping("/flujo-caja")
    public String listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) String tipo,
            Model model) {

        List<FlujoCaja> movimientos = flujoCajaService.filtrar(desde, hasta);

        model.addAttribute("movimientos", movimientos);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("tipo", tipo);

        model.addAttribute("ingresos", flujoCajaService.calcularIngresos(desde, hasta));
        model.addAttribute("egresos", flujoCajaService.calcularEgresos(desde, hasta));
        model.addAttribute("saldo", flujoCajaService.calcularSaldo(desde, hasta));
        
        // Contar ingresos y egresos
        long conteoIngresos = movimientos.stream().filter(m -> "INGRESO".equals(m.getTipoMovimiento().name())).count();
        long conteoEgresos = movimientos.stream().filter(m -> "EGRESO".equals(m.getTipoMovimiento().name())).count();
        model.addAttribute("conteoIngresos", conteoIngresos);
        model.addAttribute("conteoEgresos", conteoEgresos);

        return "flujo-caja/list";
    }
}
