package com.modacol.exe.impl;

import com.modacol.exe.entity.Compra;
import com.modacol.exe.entity.FlujoCaja;
import com.modacol.exe.entity.TipoMovimiento;
import com.modacol.exe.entity.Venta;
import com.modacol.exe.repository.FlujoCajaRepository;
import com.modacol.exe.service.FlujoCajaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FlujoCajaServiceImpl implements FlujoCajaService {

    private final FlujoCajaRepository flujoCajaRepository;

    public FlujoCajaServiceImpl(FlujoCajaRepository flujoCajaRepository) {
        this.flujoCajaRepository = flujoCajaRepository;
    }

    @Override
    @Transactional
    public FlujoCaja registrarIngresoPorVenta(Venta venta) {
        if (venta == null || venta.getId() == null) {
            throw new IllegalArgumentException("Venta inválida para registrar flujo de caja");
        }

        FlujoCaja flujo = new FlujoCaja();
        flujo.setFecha(venta.getFechaVenta());
        flujo.setTipoMovimiento(TipoMovimiento.INGRESO);
        flujo.setDescripcion("Venta " + venta.getNumeroVenta());
        flujo.setMonto(venta.getTotal() != null ? venta.getTotal() : BigDecimal.ZERO);
        flujo.setVentaId(venta.getId());
        flujo.setCompraId(null);

        return flujoCajaRepository.save(flujo);
    }

    @Override
    @Transactional
    public FlujoCaja registrarEgresoPorCompra(Compra compra) {
        if (compra == null || compra.getId() == null) {
            throw new IllegalArgumentException("Compra inválida para registrar flujo de caja");
        }

        FlujoCaja flujo = new FlujoCaja();
        flujo.setFecha(compra.getFechaCompra());
        flujo.setTipoMovimiento(TipoMovimiento.EGRESO);
        flujo.setDescripcion("Compra " + compra.getNumeroCompra());
        flujo.setMonto(compra.getTotal() != null ? compra.getTotal() : BigDecimal.ZERO);
        flujo.setVentaId(null);
        flujo.setCompraId(compra.getId());

        return flujoCajaRepository.save(flujo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlujoCaja> listar() {
        return flujoCajaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlujoCaja> filtrar(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            return flujoCajaRepository.findAll();
        }
        return flujoCajaRepository.findByFechaBetween(desde, hasta);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularIngresos(LocalDate desde, LocalDate hasta) {
        return filtrar(desde, hasta).stream()
                .filter(f -> f.getTipoMovimiento() == TipoMovimiento.INGRESO)
                .map(FlujoCaja::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularEgresos(LocalDate desde, LocalDate hasta) {
        return filtrar(desde, hasta).stream()
                .filter(f -> f.getTipoMovimiento() == TipoMovimiento.EGRESO)
                .map(FlujoCaja::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularSaldo(LocalDate desde, LocalDate hasta) {
        List<FlujoCaja> movimientos = filtrar(desde, hasta);

        BigDecimal ingresos = movimientos.stream()
                .filter(f -> f.getTipoMovimiento() == TipoMovimiento.INGRESO)
                .map(FlujoCaja::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal egresos = movimientos.stream()
                .filter(f -> f.getTipoMovimiento() == TipoMovimiento.EGRESO)
                .map(FlujoCaja::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ingresos.subtract(egresos);
    }
}
