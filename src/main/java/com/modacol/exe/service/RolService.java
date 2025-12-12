package com.modacol.exe.service;

import com.modacol.exe.dto.RolDTO;

import java.util.List;

public interface RolService {

    RolDTO crear(RolDTO dto);

    List<RolDTO> listar();

    List<RolDTO> filtrar(String tipo);

    RolDTO obtenerPorId(Long id);

    RolDTO actualizar(Long id, RolDTO dto);

    void eliminar(Long id);
}
