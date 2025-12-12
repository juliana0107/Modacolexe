package com.modacol.exe.repository;

import com.modacol.exe.entity.FlujoCaja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FlujoCajaRepository extends JpaRepository<FlujoCaja, Long> {

    List<FlujoCaja> findByFechaBetween(LocalDate desde, LocalDate hasta);
}
