package com.modacol.exe.service;

import com.modacol.exe.dto.CompraDTO;

import java.util.List;

public interface CompraService {

    CompraDTO crear(CompraDTO dto);

    CompraDTO obtenerPorId(Long id);

    List<CompraDTO> listar();

    CompraDTO actualizar(Long id, CompraDTO dto);

    void eliminar(Long id);

    CompraDTO crearCompraVacia();

    List<String> obtenerTiposDocumento();
    List<String> obtenerFormasPago();
    List<String> obtenerEstadosCompra();
}
