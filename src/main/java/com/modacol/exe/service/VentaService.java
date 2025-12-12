// com.modacol.exe.service.VentaService
package com.modacol.exe.service;

import com.modacol.exe.dto.VentaDTO;

import java.time.LocalDate;
import java.util.List;

public interface VentaService {

    VentaDTO crear(VentaDTO dto);

    VentaDTO actualizar(Long id, VentaDTO dto);

    VentaDTO obtenerPorId(Long id);

    List<VentaDTO> listar();

    void eliminar(Long id);

    List<VentaDTO> filtrar(LocalDate desde, LocalDate hasta, Long usuarioId);


    // para el form
    VentaDTO crearVentaVacia();

    List<String> obtenerTiposDocumento();

    List<String> obtenerFormasPago();

    List<String> obtenerEstadosVenta();
}
