package com.modacol.exe.repository;

import com.modacol.exe.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    Optional<Compra> findTopByOrderByNumeroCompraDesc();
    List<Compra> findAllByOrderByIdAsc();
}
