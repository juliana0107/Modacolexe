// com.modacol.exe.dto.VentaDTO
package com.modacol.exe.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class VentaDTO {

    private Long id;

    private String numeroVenta;
    private LocalDate fechaVenta;

    private Long clienteId;
    private Long usuarioId;

    private String clienteNombre;
    private String usuarioNombre;

    private String tipoDocumento;   // FACTURA, BOLETA, etc
    private String formaPago;       // CONTADO, CREDITO_30
    private String estado;          // BORRADOR, CONFIRMADA, ANULADA
    private String observaciones;

    private BigDecimal total;

    // 👇 AQUÍ el cambio importante
    private List<DetalleVentaDTO> detalles = new ArrayList<>();
}
