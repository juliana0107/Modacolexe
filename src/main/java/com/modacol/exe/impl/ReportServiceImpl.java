package com.modacol.exe.impl;

import com.modacol.exe.service.ReportService;
import com.modacol.exe.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private ProveedorRepository proveedorRepository;
    @Autowired private VentaRepository ventaRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private CompraRepository compraRepository;
    @Autowired private FlujoCajaRepository flujoCajaRepository;
    @Autowired private RolRepository rolRepository;

    private static final String FECHA_FORMATO = "dd/MM/yyyy HH:mm";

    // --- 1. USUARIOS ---
    @Override
    public void generarReporteUsuarios(OutputStream os) {
        String[] columnas = {"ID", "NOMBRE", "CORREO", "ROL", "FECHA REGISTRO"};
        generarExcelGenerico("REPORTE DE USUARIOS", columnas, os, (sheet, estilo, fecha) -> {
            var lista = usuarioRepository.findAll();
            int rowNum = 6;
            for (var u : lista) {
                Row row = sheet.createRow(rowNum++);
                crearCelda(row, 0, String.valueOf(u.getId()), estilo);
                crearCelda(row, 1, u.getNombre(), estilo);
                crearCelda(row, 2, u.getCorreo(), estilo);
                crearCelda(row, 3, u.getRol() != null ? u.getRol().getTipo() : "N/A", estilo);
                crearCelda(row, 4, fecha, estilo);
            }
        });
    }

    // --- 2. CLIENTES (CORREGIDO) ---
    @Override
    public void generarReporteClientes(OutputStream os) {
        String[] columnas = {"ID", "EMPRESA", "NOMBRE", "IDENTIFICACIÓN", "TELÉFONO", "CORREO"};
        generarExcelGenerico("REPORTE DE CLIENTES", columnas, os, (sheet, estilo, fecha) -> {
            var lista = clienteRepository.findAll();
            int rowNum = 6;
            for (var c : lista) {
                Row row = sheet.createRow(rowNum++);
                crearCelda(row, 0, String.valueOf(c.getId()), estilo);
                crearCelda(row, 1, c.getEmpresa(), estilo);
                crearCelda(row, 2, c.getNombre(), estilo);
                crearCelda(row, 3, c.getIdentificacion(), estilo);
                crearCelda(row, 4, c.getContacto(), estilo);
                crearCelda(row, 5, c.getCorreo(), estilo);
            }
        });
    }

    // --- 3. PRODUCTOS ---
    @Override
    public void generarReporteProductos(OutputStream os) {
        String[] columnas = {"ID", "NOMBRE", "PRECIO", "STOCK", "PROVEEDOR"};
        generarExcelGenerico("INVENTARIO DE PRODUCTOS", columnas, os, (sheet, estilo, fecha) -> {
            var lista = productoRepository.findAll();
            int rowNum = 6;
            for (var p : lista) {
                Row row = sheet.createRow(rowNum++);
                crearCelda(row, 0, String.valueOf(p.getId()), estilo);
                crearCelda(row, 1, p.getNombre(), estilo);
                crearCelda(row, 3, p.getPrecioUnitario() != null ? p.getPrecioUnitario().doubleValue() : 0.0, estilo, sheet.getWorkbook());
                crearCelda(row, 4, String.valueOf(p.getCantidad()), estilo);
                crearCelda(row, 5, p.getProveedor() != null ? p.getProveedor().getRazonSocial() : "N/A", estilo);
            }
        });
    }

    // --- 4. PROVEEDORES ---
    @Override
    public void generarReporteProveedores(OutputStream os) {
        String[] columnas = {"ID", "RAZÓN SOCIAL", "NIT", "TELÉFONO", "CORREO"};
        generarExcelGenerico("REPORTE DE PROVEEDORES", columnas, os, (sheet, estilo, fecha) -> {
            var lista = proveedorRepository.findAll();
            int rowNum = 6;
            for (var p : lista) {
                Row row = sheet.createRow(rowNum++);
                crearCelda(row, 0, String.valueOf(p.getId()), estilo);
                crearCelda(row, 1, p.getRazonSocial(), estilo);
                crearCelda(row, 2, String.valueOf(p.getIdentificacion()), estilo);
                crearCelda(row, 3, String.valueOf(p.getContacto()), estilo);
                crearCelda(row, 4, p.getCorreo(), estilo);
            }
        });
    }

    // --- 5. VENTAS ---
    @Override
    public void generarReporteVentas(OutputStream os) {
        String[] columnas = {"ID", "FECHA", "CLIENTE", "VENDEDOR", "TOTAL"};
        generarExcelGenerico("REPORTE DE VENTAS", columnas, os, (sheet, estilo, fecha) -> {
            var lista = ventaRepository.findAll();
            int rowNum = 6;
            for (var v : lista) {
                Row row = sheet.createRow(rowNum++);
                crearCelda(row, 0, String.valueOf(v.getId()), estilo);
                crearCelda(row, 1, v.getFechaVenta() != null ? v.getFechaVenta().toString() : "N/A", estilo);
                crearCelda(row, 2, v.getCliente() != null ? v.getCliente().getNombre() : "General", estilo);
                crearCelda(row, 3, v.getUsuario() != null ? v.getUsuario().getNombre() : "N/A", estilo);
                crearCelda(row, 4, v.getTotal() != null ? v.getTotal().doubleValue() : 0.0, estilo, sheet.getWorkbook());
            }
        });
    }

    // --- 6. COMPRAS (CORREGIDO) ---
    @Override
    public void generarReporteCompras(OutputStream os) {
        String[] columnas = {"ID", "FECHA COMPRA", "PROVEEDOR", "ESTADO", "TOTAL"};
        generarExcelGenerico("REPORTE DE COMPRAS", columnas, os, (sheet, estilo, fecha) -> {
            var lista = compraRepository.findAll();
            int rowNum = 6;
            for (var c : lista) {
                Row row = sheet.createRow(rowNum++);
                crearCelda(row, 0, String.valueOf(c.getId()), estilo);
                crearCelda(row, 1, c.getFechaCompra() != null ? c.getFechaCompra().toString() : "N/A", estilo);
                crearCelda(row, 2, c.getProveedor() != null ? c.getProveedor().getRazonSocial() : "N/A", estilo);
                crearCelda(row, 3, c.getEstado() != null ? c.getEstado() : "COMPLETADO", estilo);
                crearCelda(row, 4, c.getTotal() != null ? c.getTotal().doubleValue() : 0.0, estilo, sheet.getWorkbook());
            }
        });
    }

    // --- 7. CATEGORÍAS ---
    @Override
    public void generarReporteCategorias(OutputStream os) {
        String[] columnas = {"ID", "NOMBRE CATEGORÍA"};
        generarExcelGenerico("REPORTE DE CATEGORÍAS", columnas, os, (sheet, estilo, fecha) -> {
            var lista = categoriaRepository.findAll();
            int rowNum = 6;
            for (var c : lista) {
                Row row = sheet.createRow(rowNum++);
                crearCelda(row, 0, String.valueOf(c.getId()), estilo);
                crearCelda(row, 1, c.getTipoCategoria(), estilo);
            }
        });
    }

    // --- 8. FLUJO DE CAJA ---
    @Override
    public void generarReporteFlujo(OutputStream os) {
        String[] columnas = {"ID", "FECHA", "DESCRIPCIÓN", "TIPO", "MONTO"};
        generarExcelGenerico("REPORTE FLUJO DE CAJA", columnas, os, (sheet, estilo, fecha) -> {
            var lista = flujoCajaRepository.findAll();
            int rowNum = 6;
            for (var f : lista) {
                Row row = sheet.createRow(rowNum++);
                crearCelda(row, 0, String.valueOf(f.getId()), estilo);
                crearCelda(row, 1, f.getFecha() != null ? f.getFecha().toString() : "N/A", estilo);
                crearCelda(row, 2, f.getDescripcion(), estilo);
                crearCelda(row, 3, f.getTipoMovimiento() != null ? f.getTipoMovimiento().name() : "N/A", estilo);
                crearCelda(row, 4, f.getMonto() != null ? f.getMonto().doubleValue() : 0.0, estilo, sheet.getWorkbook());
            }
        });
    }

    // --- 9. ROLES (NUEVO) ---
    @Override
    public void generarReporteRoles(OutputStream os) {
        String[] columnas = {"ID", "TIPO DE ROL"};
        generarExcelGenerico("REPORTE DE ROLES", columnas, os, (sheet, estilo, fecha) -> {
            var lista = rolRepository.findAll();
            int rowNum = 6;
            for (var r : lista) {
                Row row = sheet.createRow(rowNum++);
                crearCelda(row, 0, String.valueOf(r.getId()), estilo);
                crearCelda(row, 1, r.getTipo(), estilo);
            }
        });
    }

    // --- MÉTODOS DE DISEÑO Y ESTILOS ---

    private void generarExcelGenerico(String titulo, String[] columnas, OutputStream os, ReportDataFiller filler) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte");
            sheet.setDisplayGridlines(false);
            aplicarEstiloCorporativo(workbook, sheet, titulo, columnas);

            CellStyle estiloDatos = workbook.createCellStyle();
            estiloDatos.setBorderBottom(BorderStyle.THIN);
            estiloDatos.setBorderTop(BorderStyle.THIN);
            estiloDatos.setBorderRight(BorderStyle.THIN);
            estiloDatos.setBorderLeft(BorderStyle.THIN);

            String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FECHA_FORMATO));
            filler.fill(sheet, estiloDatos, fechaActual);

            for (int i = 0; i < columnas.length; i++) sheet.autoSizeColumn(i);
            workbook.write(os);
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    private void aplicarEstiloCorporativo(Workbook workbook, Sheet sheet, String titulo, String[] columnas) {
        try {
            InputStream is = new ClassPathResource("static/images/logo.png").getInputStream();
            int pictureIdx = workbook.addPicture(IOUtils.toByteArray(is), Workbook.PICTURE_TYPE_PNG);
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
            anchor.setCol1(0); anchor.setRow1(0);
            drawing.createPicture(anchor, pictureIdx).resize(1.5, 3.5);
        } catch (Exception ignored) {}

        Row rowTit = sheet.createRow(2);
        Cell cellTit = rowTit.createCell(2);
        cellTit.setCellValue(titulo);
        CellStyle stTit = workbook.createCellStyle();
        Font fTit = workbook.createFont();
        fTit.setBold(true); fTit.setFontHeightInPoints((short) 18); fTit.setColor(IndexedColors.DARK_BLUE.getIndex());
        stTit.setFont(fTit); cellTit.setCellStyle(stTit);

        CellStyle stHead = workbook.createCellStyle();
        stHead.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        stHead.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font fHead = workbook.createFont();
        fHead.setColor(IndexedColors.WHITE.getIndex()); fHead.setBold(true);
        stHead.setFont(fHead);

        Row hRow = sheet.createRow(5);
        for (int i = 0; i < columnas.length; i++) {
            Cell c = hRow.createCell(i);
            c.setCellValue(columnas[i]);
            c.setCellStyle(stHead);
        }
    }

    private void crearCelda(Row row, int col, String valor, CellStyle estilo) {
        Cell c = row.createCell(col);
        c.setCellValue(valor != null ? valor : "");
        c.setCellStyle(estilo);
    }

    private void crearCelda(Row row, int col, Double valor, CellStyle estiloBase, Workbook wb) {
        Cell c = row.createCell(col);
        c.setCellValue(valor != null ? valor : 0.0);
        CellStyle moneda = wb.createCellStyle();
        moneda.cloneStyleFrom(estiloBase);
        moneda.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("$#,##0.00"));
        c.setCellStyle(moneda);
    }

    @FunctionalInterface
    interface ReportDataFiller {
        void fill(Sheet sheet, CellStyle estilo, String fecha) throws Exception;
    }
}