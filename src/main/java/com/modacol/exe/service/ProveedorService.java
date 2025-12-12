package com.modacol.exe.service;

import com.modacol.exe.dto.ProveedorDTO;

import java.util.List;

public interface ProveedorService {

    ProveedorDTO crear(ProveedorDTO dto);

    List<ProveedorDTO> listar();

    List<ProveedorDTO> filtrar(String razonSocial, String identificacion, String correo);

    ProveedorDTO obtenerPorId(Long id);

    ProveedorDTO actualizar(Long id, ProveedorDTO dto);

    void eliminar(Long id);
}
