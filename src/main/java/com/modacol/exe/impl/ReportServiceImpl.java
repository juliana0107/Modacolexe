package com.modacol.exe.impl;

import com.modacol.exe.service.ReportService;
import com.modacol.exe.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private FlujoCajaRepository flujoCajaRepository;

    @Override
    public void generarReporteUsuarios(OutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Usuarios");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Nombre");
            headerRow.createCell(2).setCellValue("Correo");
            headerRow.createCell(3).setCellValue("Rol");
            headerRow.createCell(4).setCellValue("Fecha Generación");

            var usuarios = usuarioRepository.findAll();
            int rowNum = 1;
            String fechaGeneracion = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            for (var usuario : usuarios) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(usuario.getId() != null ? usuario.getId().toString() : "");
                row.createCell(1).setCellValue(usuario.getNombre() != null ? usuario.getNombre() : "");
                row.createCell(2).setCellValue(usuario.getCorreo() != null ? usuario.getCorreo() : "");

                row.createCell(3).setCellValue(
                        usuario.getRol() != null ? usuario.getRol().getTipo() : ""
                );

                row.createCell(4).setCellValue(fechaGeneracion);
            }

            for (int i = 0; i < 5; i++) sheet.autoSizeColumn(i);

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error generando reporte de usuarios", e);
        }
    }

    @Override
    public void generarReporteClientes(OutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Clientes");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Empresa");
            headerRow.createCell(2).setCellValue("Nombre");
            headerRow.createCell(3).setCellValue("Identificación");
            headerRow.createCell(4).setCellValue("Contacto");
            headerRow.createCell(5).setCellValue("Correo");
            headerRow.createCell(6).setCellValue("Fecha Generación");

            var clientes = clienteRepository.findAll();
            int rowNum = 1;
            String fechaGeneracion = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            for (var cliente : clientes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(cliente.getId() != null ? cliente.getId().toString() : "");
                row.createCell(1).setCellValue(cliente.getEmpresa() != null ? cliente.getEmpresa() : "");
                row.createCell(2).setCellValue(cliente.getNombre() != null ? cliente.getNombre() : "");
                row.createCell(3).setCellValue(cliente.getIdentificacion() != null ? cliente.getIdentificacion() : "");
                row.createCell(4).setCellValue(cliente.getContacto() != null ? cliente.getContacto() : "");
                row.createCell(5).setCellValue(cliente.getCorreo() != null ? cliente.getCorreo() : "");
                row.createCell(6).setCellValue(fechaGeneracion);
            }

            for (int i = 0; i < 7; i++) sheet.autoSizeColumn(i);

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error generando reporte de clientes", e);
        }
    }

    @Override
    public void generarReporteProductos(OutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Productos");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Nombre");
            headerRow.createCell(2).setCellValue("Descripción");
            headerRow.createCell(3).setCellValue("Precio");
            headerRow.createCell(4).setCellValue("Cantidad");
            headerRow.createCell(5).setCellValue("Proveedor");
            headerRow.createCell(6).setCellValue("Fecha Generación");

            var productos = productoRepository.findAll();
            int rowNum = 1;
            String fechaGeneracion = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            for (var producto : productos) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(producto.getId() != null ? producto.getId().toString() : "");
                row.createCell(1).setCellValue(producto.getNombre() != null ? producto.getNombre() : "");
                row.createCell(2).setCellValue(producto.getDescripcion() != null ? producto.getDescripcion() : "");
                row.createCell(3).setCellValue(producto.getPrecioUnitario() != null ? producto.getPrecioUnitario().toString() : "0");
                row.createCell(4).setCellValue(producto.getCantidad() != null ? producto.getCantidad().toString() : "0");
                row.createCell(5).setCellValue(producto.getProveedor() != null ? producto.getProveedor().getRazonSocial() : "");
                row.createCell(6).setCellValue(fechaGeneracion);
            }

            for (int i = 0; i < 7; i++) sheet.autoSizeColumn(i);

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error generando reporte de productos", e);
        }
    }

    @Override
    public void generarReporteProveedores(OutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Proveedores");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Razón Social");
            headerRow.createCell(2).setCellValue("Identificación");
            headerRow.createCell(3).setCellValue("Dirección");
            headerRow.createCell(4).setCellValue("Correo");
            headerRow.createCell(5).setCellValue("Contacto");
            headerRow.createCell(6).setCellValue("Fecha Generación");

            var proveedores = proveedorRepository.findAll();
            int rowNum = 1;
            String fechaGeneracion = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));


            for (var proveedor : proveedores) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(proveedor.getId() != null ? proveedor.getId().toString() : "");
                row.createCell(1).setCellValue(proveedor.getRazonSocial() != null ? proveedor.getRazonSocial() : "");
                row.createCell(2).setCellValue(proveedor.getIdentificacion() != null ? proveedor.getIdentificacion().toString() : "0");
                row.createCell(3).setCellValue(proveedor.getDireccion() != null ? proveedor.getDireccion() : "");
                row.createCell(4).setCellValue(proveedor.getCorreo() != null ? proveedor.getCorreo() : "");
                row.createCell(5).setCellValue(proveedor.getContacto() != null ? proveedor.getContacto().toString() : "0");

                row.createCell(6).setCellValue(fechaGeneracion);
            }


            for (int i = 0; i < 7; i++) sheet.autoSizeColumn(i);

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error generando reporte de proveedores", e);
        }
    }

    @Override
    public void generarReporteVentas(OutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ventas");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Fecha");
            headerRow.createCell(2).setCellValue("Cliente");
            headerRow.createCell(3).setCellValue("Usuario");
            headerRow.createCell(4).setCellValue("Total");
            headerRow.createCell(5).setCellValue("Fecha Generación");

            var ventas = ventaRepository.findAll();
            int rowNum = 1;
            String fechaGeneracion = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            for (var venta : ventas) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(venta.getId() != null ? venta.getId().toString() : "");
                row.createCell(1).setCellValue(venta.getFechaVenta() != null ? venta.getFechaVenta().toString() : "");
                row.createCell(2).setCellValue(venta.getCliente() != null ? venta.getCliente().getNombre() : "");
                row.createCell(3).setCellValue(venta.getUsuario() != null ? venta.getUsuario().getNombre() : "");
                row.createCell(4).setCellValue(venta.getTotal() != null ? venta.getTotal().toString() : "0");
                row.createCell(5).setCellValue(fechaGeneracion);
            }

            for (int i = 0; i < 6; i++) sheet.autoSizeColumn(i);

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error generando reporte de ventas", e);
        }
    }

    @Override
    public void generarReporteCompras(OutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Compras");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Fecha Inicio");
            headerRow.createCell(2).setCellValue("Fecha Entrega");
            headerRow.createCell(3).setCellValue("Proveedor");
            headerRow.createCell(4).setCellValue("Usuario");
            headerRow.createCell(5).setCellValue("Total");
            headerRow.createCell(6).setCellValue("Fecha Generación");

            var compras = compraRepository.findAll();
            int rowNum = 1;
            String fechaGeneracion = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            for (var compra : compras) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(compra.getId() != null ? compra.getId().toString() : "");
                row.createCell(1).setCellValue(compra.getFechaCompra() != null ? compra.getFechaCompra().toString() : "");
                row.createCell(2).setCellValue(compra.getFechaEstimadaEntrega() != null ? compra.getFechaEstimadaEntrega().toString() : "");
                row.createCell(3).setCellValue(compra.getProveedor() != null ? compra.getProveedor().getRazonSocial() : "");
                row.createCell(4).setCellValue(compra.getUsuario() != null ? compra.getUsuario().getNombre() : "");
                row.createCell(5).setCellValue(compra.getTotal() != null ? compra.getTotal().toString() : "0");
                row.createCell(6).setCellValue(fechaGeneracion);
            }

            for (int i = 0; i < 7; i++) sheet.autoSizeColumn(i);

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error generando reporte de compras", e);
        }
    }

    @Override
    public void generarReporteCategorias(OutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Categorías");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Tipo de Categoría");
            headerRow.createCell(2).setCellValue("Fecha Generación");

            var categorias = categoriaRepository.findAll();
            int rowNum = 1;
            String fechaGeneracion = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            for (var categoria : categorias) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(categoria.getId() != null ? categoria.getId().toString() : "");
                row.createCell(1).setCellValue(categoria.getTipoCategoria() != null ? categoria.getTipoCategoria() : "");
                row.createCell(2).setCellValue(fechaGeneracion);
            }

            for (int i = 0; i < 3; i++) sheet.autoSizeColumn(i);

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error generando reporte de categorías", e);
        }
    }

    @Override
    public void generarReporteFlujo(OutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Flujo de Caja");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Fecha");
            headerRow.createCell(2).setCellValue("Descripción");
            headerRow.createCell(3).setCellValue("Tipo");
            headerRow.createCell(4).setCellValue("Monto");
            headerRow.createCell(5).setCellValue("Origen");
            headerRow.createCell(6).setCellValue("Fecha Generación");

            var flujos = flujoCajaRepository.findAll();
            int rowNum = 1;
            String fechaGeneracion = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            for (var flujo : flujos) {
                Row row = sheet.createRow(rowNum++);

                // ID
                row.createCell(0).setCellValue(
                        flujo.getId() != null ? flujo.getId().toString() : ""
                );

                // Fecha
                row.createCell(1).setCellValue(
                        flujo.getFecha() != null ? flujo.getFecha().toString() : ""
                );

                // Descripción
                row.createCell(2).setCellValue(
                        flujo.getDescripcion() != null ? flujo.getDescripcion() : ""
                );

                // Tipo (INGRESO / EGRESO)  ← OJO: ahora usamos tipoMovimiento
                row.createCell(3).setCellValue(
                        flujo.getTipoMovimiento() != null ? flujo.getTipoMovimiento().name() : ""
                );

                // Monto
                row.createCell(4).setCellValue(
                        flujo.getMonto() != null ? flujo.getMonto().doubleValue() : 0.0
                );

                // Origen (Venta / Compra / Manual)
                String origen;
                if (flujo.getVentaId() != null) {
                    origen = "Venta #" + flujo.getVentaId();
                } else if (flujo.getCompraId() != null) {
                    origen = "Compra #" + flujo.getCompraId();
                } else {
                    origen = "Movimiento manual";
                }
                row.createCell(5).setCellValue(origen);

                // Fecha de generación del reporte
                row.createCell(6).setCellValue(fechaGeneracion);
            }

            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error generando reporte de flujo de caja", e);
        }
    }
}

