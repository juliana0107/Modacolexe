// com.modacol.exe.dto.DetalleVentaDTO
package com.modacol.exe.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleVentaDTO {

    private Long id;
    private Long ventaId;

    private Long productoId;
    private String productoNombre;

    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
