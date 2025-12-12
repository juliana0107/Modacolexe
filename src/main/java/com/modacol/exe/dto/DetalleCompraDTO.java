package com.modacol.exe.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class DetalleCompraDTO {

    private Long id;
    private Long compraId;

    @NotNull(message = "Debe seleccionar un producto")
    private Long productoId;

    private String productoNombre;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio unitario debe ser mayor que 0")
    private BigDecimal precioUnitario;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que 0")
    private Integer cantidad;

    // Este debería ser calculado en backend: precioUnitario * cantidad
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal subtotal;
}
