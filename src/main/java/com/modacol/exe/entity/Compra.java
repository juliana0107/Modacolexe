package com.modacol.exe.entity;

import jakarta.persistence.*;
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

    @Column(name = "numero_compra", length = 30, unique = true)
    private String numeroCompra;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDate fechaCompra;

    @Column(name = "fecha_estimada_entrega")
    private LocalDate fechaEstimadaEntrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", referencedColumnName = "id", nullable = false)
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    @Column(name = "tipo_documento", length = 30, nullable = false)
    private String tipoDocumento; // Podrías luego migrar a Enum

    @Column(name = "forma_pago", length = 30, nullable = false)
    private String formaPago;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

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
