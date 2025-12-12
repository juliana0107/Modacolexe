package com.modacol.exe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "compras")
@Data
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de compra es obligatorio")
    @Size(max = 30, message = "El número de compra no puede exceder 30 caracteres")
    @Column(name = "numero_compra", length = 30, unique = true)
    private String numeroCompra;

    @NotNull(message = "La fecha de compra es obligatoria")
    @Column(name = "fecha_compra", nullable = false)
    private LocalDate fechaCompra;

    @Column(name = "fecha_estimada_entrega")
    private LocalDate fechaEstimadaEntrega;

    @NotNull(message = "El proveedor es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", referencedColumnName = "id", nullable = false)
    private Proveedor proveedor;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 30, message = "El tipo de documento no puede exceder 30 caracteres")
    @Column(name = "tipo_documento", length = 30, nullable = false)
    private String tipoDocumento;

    @NotBlank(message = "La forma de pago es obligatoria")
    @Size(max = 30, message = "La forma de pago no puede exceder 30 caracteres")
    @Column(name = "forma_pago", length = 30, nullable = false)
    private String formaPago;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no puede exceder 20 caracteres")
    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    @Size(max = 255, message = "Las observaciones no pueden exceder 255 caracteres")
    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(
            mappedBy = "compra",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<DetalleCompra> detalles;

    /** Conveniencia: recalcular total antes de guardar */
    @PrePersist
    @PreUpdate
    public void calcularTotal() {
        if (detalles == null || detalles.isEmpty()) {
            this.total = BigDecimal.ZERO;
            return;
        }

        this.total = detalles.stream()
                .map(DetalleCompra::getSubtotal)
                .filter(sub -> sub != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
