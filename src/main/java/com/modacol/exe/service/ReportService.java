package com.modacol.exe.service;

import java.io.OutputStream;

public interface ReportService {
    void generarReporteUsuarios(OutputStream outputStream);
    void generarReporteClientes(OutputStream outputStream);
    void generarReporteProductos(OutputStream outputStream);
    void generarReporteProveedores(OutputStream outputStream);
    void generarReporteVentas(OutputStream outputStream);
    void generarReporteCompras(OutputStream outputStream);
    void generarReporteCategorias(OutputStream outputStream);
    void generarReporteFlujo(OutputStream outputStream);
}