package com.modacol.exe.repository;

import com.modacol.exe.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long>, JpaSpecificationExecutor<Venta> {
    @Query("SELECT v FROM Venta v WHERE v.fechaVenta BETWEEN :desde AND :hasta")
    List<Venta> filtrarPorFechas(@Param("desde") LocalDate desde,
                                 @Param("hasta") LocalDate hasta);

}
