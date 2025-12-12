package com.modacol.exe.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductoDTO {

    private Long id;

    private LocalDate fecha;
    private String nombre;
    private String descripcion;
    private BigDecimal precioUnitario;
    private Integer cantidad = 0;

    private Long categoriaId;
    private String categoriaNombre;

    private Long proveedorId;
    private String proveedorNombre;
}
