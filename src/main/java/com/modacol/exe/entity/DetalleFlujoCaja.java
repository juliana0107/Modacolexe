package com.modacol.exe.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "detalle_flujo_caja")
@Data
public class DetalleFlujoCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_movimiento")
    private LocalDate fechaMovimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", length = 10)
    private TipoMovimiento tipoMovimiento;

    @Column(name = "monto", precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "metodo_pago", length = 20)
    private String metodoPago;

    // VENTA AUTOMÁTICA
    @Column(name = "venta_id")
    private Long ventaId;

    // COMPRA AUTOMÁTICA
    @Column(name = "compra_id")
    private Long compraId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "comprobante_url")
    private String comprobanteUrl;

}
