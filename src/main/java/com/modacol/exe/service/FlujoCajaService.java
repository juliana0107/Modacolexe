package com.modacol.exe.service;

import com.modacol.exe.entity.Compra;
import com.modacol.exe.entity.FlujoCaja;
import com.modacol.exe.entity.Venta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlujoCajaService {

    FlujoCaja registrarIngresoPorVenta(Venta venta);

    FlujoCaja registrarEgresoPorCompra(Compra compra);

    List<FlujoCaja> listar();

    BigDecimal calcularIngresos(LocalDate desde, LocalDate hasta);

    BigDecimal calcularEgresos(LocalDate desde, LocalDate hasta);

    List<FlujoCaja> filtrar(LocalDate desde, LocalDate hasta);

    BigDecimal calcularSaldo(LocalDate desde, LocalDate hasta);
}
