package com.modacol.exe.service;

import com.modacol.exe.dto.ClienteDTO;

import java.util.List;

public interface ClienteService {

    ClienteDTO crear(ClienteDTO dto);

    List<ClienteDTO> listar();

    List<ClienteDTO> filtrar(String empresa, String nombre, String identificacion, String correo);

    ClienteDTO obtenerPorId(Long id);

    ClienteDTO actualizar(Long id, ClienteDTO dto);

    void eliminar(Long id);
}
