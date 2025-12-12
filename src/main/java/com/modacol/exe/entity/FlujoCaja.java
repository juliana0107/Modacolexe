package com.modacol.exe.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "flujo_caja")
@Data
public class FlujoCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", length = 10, nullable = false)
    private TipoMovimiento tipoMovimiento;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "monto", precision = 12, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(name = "venta_id")
    private Long ventaId;

    @Column(name = "compra_id")
    private Long compraId;
}
