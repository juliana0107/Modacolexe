package com.modacol.exe.controller;

import com.modacol.exe.dto.VentaDTO;
import com.modacol.exe.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class DashboardController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private ClienteService clienteService;
    @Autowired private ProductoService productoService;
    @Autowired private ProveedorService proveedorService;
    @Autowired private VentaService ventaService;
    @Autowired private CompraService compraService;
    @Autowired private FlujoCajaService flujoCajaService;


    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        // ---------- Contadores básicos ----------
        model.addAttribute("totalUsuarios", usuarioService.listar().size());
        model.addAttribute("totalClientes", clienteService.listar().size());
        model.addAttribute("totalProductos", productoService.listar().size());
        model.addAttribute("totalProveedores", proveedorService.listar().size());

        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);

        // ---------- Ventas hoy / mes ----------
        List<VentaDTO> ventasHoy = ventaService.filtrar(hoy, hoy, null);
        List<VentaDTO> ventasMes = ventaService.filtrar(inicioMes, hoy, null);

        model.addAttribute("ventasHoy", ventasHoy.size());
        model.addAttribute("ventasMes", ventasMes.size());

        // Ingresos del mes
        BigDecimal ingresosMes = ventasMes.stream()
                .map(v -> v.getTotal() != null ? v.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("ingresosMes", ingresosMes);

        // ---------- Productos con stock bajo ----------
        long productosStockBajo = productoService.listar().stream()
                .filter(p -> p.getCantidad() != null && p.getCantidad() < 10)
                .count();
        model.addAttribute("productosStockBajo", productosStockBajo);

        // ---------- Últimas ventas ----------
        List<VentaDTO> ultimasVentas = ventaService.listar().stream()
                .sorted((v1, v2) -> {
                    LocalDate f1 = v1.getFechaVenta();
                    LocalDate f2 = v2.getFechaVenta();
                    if (f1 == null && f2 == null) return 0;
                    if (f1 == null) return 1;
                    if (f2 == null) return -1;
                    return f2.compareTo(f1); // descendente
                })
                .limit(5)
                .toList();
        model.addAttribute("ultimasVentas", ultimasVentas);

        // ---------- Productos más “populares” (menos stock) ----------
        var productosPopulares = productoService.listar().stream()
                .sorted(Comparator.comparingInt(p -> p.getCantidad() != null ? p.getCantidad() : 0))
                .limit(5)
                .toList();
        model.addAttribute("productosPopulares", productosPopulares);

        // ---------- Datos para el GRÁFICO (ventas por día del mes) ----------
        // Usamos TreeMap para que ya queden ordenadas por fecha
        Map<LocalDate, BigDecimal> totalesPorDia = new TreeMap<>();

        for (VentaDTO v : ventasMes) {
            LocalDate fecha = v.getFechaVenta();
            if (fecha == null) continue;

            BigDecimal total = v.getTotal() != null ? v.getTotal() : BigDecimal.ZERO;
            totalesPorDia.merge(fecha, total, BigDecimal::add);
        }

        List<String> chartLabels = new ArrayList<>();
        List<BigDecimal> chartData = new ArrayList<>();

        // Etiquetas tipo "11/12", "12/12", etc.
        totalesPorDia.forEach((fecha, total) -> {
            String label = fecha.getDayOfMonth() + "/" + fecha.getMonthValue();
            chartLabels.add(label);
            chartData.add(total);
        });

        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        return "dashboard/index";
    }
}
