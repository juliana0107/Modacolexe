package com.modacol.exe.service;

import com.modacol.exe.entity.FlujoCaja;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardService {

    BigDecimal obtenerSaldoActual();

    BigDecimal obtenerTotalIngresosMes();

    BigDecimal obtenerTotalEgresosMes();

    Long contarMovimientosMes();

    List<FlujoCaja> ultimosMovimientos(int limite);
}
