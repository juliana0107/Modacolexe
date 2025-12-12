package com.modacol.exe.repository;

import com.modacol.exe.entity.DetalleFlujoCaja;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleFlujoCajaRepository extends JpaRepository<DetalleFlujoCaja, Long> {


    void deleteByVentaId(Long ventaId);

    void deleteByCompraId(Long compraId);
}
