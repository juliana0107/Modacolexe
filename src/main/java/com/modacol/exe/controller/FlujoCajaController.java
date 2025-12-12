package com.modacol.exe.controller;

import com.modacol.exe.entity.FlujoCaja;
import com.modacol.exe.service.FlujoCajaService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flujo-caja")
public class FlujoCajaController {

    private final FlujoCajaService flujoCajaService;

    public FlujoCajaController(FlujoCajaService flujoCajaService) {
        this.flujoCajaService = flujoCajaService;
    }

    @GetMapping
    public List<FlujoCaja> listar() {
        return flujoCajaService.listar();
    }

    @GetMapping("/filtrar")
    public List<FlujoCaja> filtrar(@RequestParam LocalDate desde,
                                   @RequestParam LocalDate hasta) {
        return flujoCajaService.filtrar(desde, hasta);
    }

    @GetMapping("/saldo")
    public String saldo(@RequestParam LocalDate desde,
                        @RequestParam LocalDate hasta) {

        return "Saldo: " + flujoCajaService.calcularSaldo(desde, hasta);
    }
}
