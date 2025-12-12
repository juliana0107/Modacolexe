package com.modacol.exe.dto;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class CompraDTO {

    private Long id;

    // Número de compra tipo PO-000123
    @Size(max = 30)
    private String numeroCompra;

    @NotNull(message = "La fecha de compra es obligatoria")
    private LocalDate fechaCompra;

    @FutureOrPresent(message = "La fecha estimada de entrega no puede ser en el pasado")
    private LocalDate fechaEstimadaEntrega;

    @NotNull(message = "Debe seleccionar un proveedor")
    private Long proveedorId;

    @NotNull(message = "Debe seleccionar un usuario responsable")
    private Long usuarioId;

    //  para mostrar en la vista
    private String proveedorNombre;
    private String usuarioNombre;

    // Podrías luego mapear esto a enums en el backend
    @NotBlank(message = "Debe seleccionar un tipo de documento")
    private String tipoDocumento; // ORDEN_COMPRA, FACTURA, etc.

    @NotBlank(message = "Debe seleccionar una forma de pago")
    private String formaPago; // CONTADO, CREDITO_30, etc.

    @NotBlank(message = "Debe indicar el estado de la compra")
    private String estado;

    @Size(max = 255, message = "Las observaciones no pueden superar 255 caracteres")
    private String observaciones;

    // Total calculado, no debería venir del usuario
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal total;

    @NotEmpty(message = "La compra debe tener al menos un producto en el detalle")
    @Valid
    private List<DetalleCompraDTO> detalles = new ArrayList<>();

}
