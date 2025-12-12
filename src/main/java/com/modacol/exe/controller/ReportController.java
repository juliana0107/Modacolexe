package com.modacol.exe.controller;

import com.modacol.exe.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/reportes")
    public void generarReporte(@RequestParam String tipo, HttpServletResponse response) throws IOException {
        try {
            // Configurar headers para descarga Excel
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=reporte_" + tipo + ".xlsx");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            
            // Generar reporte según tipo
            switch (tipo.toLowerCase()) {
                case "usuarios":
                    reportService.generarReporteUsuarios(response.getOutputStream());
                    break;
                case "clientes":
                    reportService.generarReporteClientes(response.getOutputStream());
                    break;
                case "productos":
                    reportService.generarReporteProductos(response.getOutputStream());
                    break;
                case "proveedores":
                    reportService.generarReporteProveedores(response.getOutputStream());
                    break;
                case "ventas":
                    reportService.generarReporteVentas(response.getOutputStream());
                    break;
                case "compras":
                    reportService.generarReporteCompras(response.getOutputStream());
                    break;
                case "categorias":
                    reportService.generarReporteCategorias(response.getOutputStream());
                    break;
                case "flujo-caja":
                    reportService.generarReporteFlujo(response.getOutputStream());
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tipo de reporte no válido");
                    return;
            }
            
            response.flushBuffer();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generando reporte: " + e.getMessage());
        }
    }
}