// com.modacol.exe.entity.Venta
package com.modacol.exe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de venta es obligatorio")
    @Size(max = 20, message = "El número de venta no puede exceder 20 caracteres")
    @Column(name = "numero_venta", unique = true, length = 20)
    private String numeroVenta;

    @NotNull(message = "La fecha de venta es obligatoria")
    @Column(name = "fecha_venta")
    private LocalDate fechaVenta;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 30, message = "El tipo de documento no puede exceder 30 caracteres")
    @Column(name = "tipo_documento", length = 30)
    private String tipoDocumento;

    @NotBlank(message = "La forma de pago es obligatoria")
    @Size(max = 30, message = "La forma de pago no puede exceder 30 caracteres")
    @Column(name = "forma_pago", length = 30)
    private String formaPago;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 30, message = "El estado no puede exceder 30 caracteres")
    @Column(name = "estado", length = 30)
    private String estado;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();
}
