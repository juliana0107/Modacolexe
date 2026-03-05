package com.modacol.exe.controller;

import com.modacol.exe.dto.ProductoDTO;
import com.modacol.exe.dto.VentaDTO;
import com.modacol.exe.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private ClienteService clienteService;
    @Autowired private ProductoService productoService;
    @Autowired private ProveedorService proveedorService;
    @Autowired private VentaService ventaService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        // 1. Cargamos productos una sola vez para optimizar
        List<ProductoDTO> todosLosProductos = productoService.listar();

        // ---------- Contadores básicos ----------
        model.addAttribute("totalUsuarios", usuarioService.listar().size());
        model.addAttribute("totalClientes", clienteService.listar().size());
        model.addAttribute("totalProductos", todosLosProductos.size());
        model.addAttribute("totalProveedores", proveedorService.listar().size());

        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);

        // ---------- Ventas hoy / mes ----------
        List<VentaDTO> ventasHoy = ventaService.filtrar(hoy, hoy, null);
        List<VentaDTO> ventasMes = ventaService.filtrar(inicioMes, hoy, null);

        model.addAttribute("ventasHoy", ventasHoy.size());
        model.addAttribute("ventasMes", ventasMes.size());

        // Ingresos del mes (COP)
        BigDecimal ingresosMes = ventasMes.stream()
                .map(v -> v.getTotal() != null ? v.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("ingresosMes", ingresosMes);

        // ---------- Productos con stock crítico (Menos de 10) ----------
        long productosStockBajo = todosLosProductos.stream()
                .filter(p -> p.getCantidad() != null && p.getCantidad() < 10)
                .count();
        model.addAttribute("productosStockBajo", productosStockBajo);

        // ---------- Últimas ventas (Historial) ----------
        List<VentaDTO> ultimasVentas = ventaService.listar().stream()
                .sorted((v1, v2) -> {
                    LocalDate f1 = v1.getFechaVenta() != null ? v1.getFechaVenta() : LocalDate.MIN;
                    LocalDate f2 = v2.getFechaVenta() != null ? v2.getFechaVenta() : LocalDate.MIN;
                    return f2.compareTo(f1); // Descendente: más recientes primero
                })
                .limit(5)
                .toList();
        model.addAttribute("ultimasVentas", ultimasVentas);

        // ---------- Tabla de Stock Crítico (Los 5 más bajos) ----------
        // Aquí aseguramos que el DTO pase datos limpios a la vista
        var productosPopulares = todosLosProductos.stream()
                .sorted(Comparator.comparingInt(p -> p.getCantidad() != null ? p.getCantidad() : 0))
                .limit(5)
                .toList();
        model.addAttribute("productosPopulares", productosPopulares);

        // ---------- Datos para el GRÁFICO (Chart.js) ----------
        Map<LocalDate, BigDecimal> totalesPorDia = new TreeMap<>();
        for (VentaDTO v : ventasMes) {
            if (v.getFechaVenta() != null) {
                BigDecimal total = v.getTotal() != null ? v.getTotal() : BigDecimal.ZERO;
                totalesPorDia.merge(v.getFechaVenta(), total, BigDecimal::add);
            }
        }

        List<String> chartLabels = new ArrayList<>();
        List<Double> chartData = new ArrayList<>(); // Usamos Double para compatibilidad con JavaScript

        totalesPorDia.forEach((fecha, total) -> {
            chartLabels.add(fecha.getDayOfMonth() + "/" + fecha.getMonthValue());
            chartData.add(total.doubleValue());
        });

        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        return "dashboard/index";
    }
}