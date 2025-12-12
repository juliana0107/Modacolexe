package com.modacol.exe.service;

import com.modacol.exe.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioDTO crear(UsuarioDTO dto);

    List<UsuarioDTO> listar();

    List<UsuarioDTO> filtrar(String nombre, String correo, Long rolId);

    UsuarioDTO obtenerPorId(Long id);

    UsuarioDTO actualizar(Long id, UsuarioDTO dto);

    void eliminar(Long id);
}
