package com.modacol.exe.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DetalleFlujoCajaDTO {

    private Long id;
    private LocalDate fechaMovimiento;
    private String tipoMovimiento;
    private Double monto;
    private String metodoPago;
    private Long clienteId;
    private String clienteNombre;
    private Long proveedorId;
    private String proveedorNombre;
    private Long categoriaId;
    private String categoriaNombre;
    private String observacion;
    private String comprobanteUrl;
}
