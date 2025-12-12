package com.modacol.exe.service;

import com.modacol.exe.dto.CategoriaDTO;

import java.util.List;

public interface CategoriaService {

    CategoriaDTO crear(CategoriaDTO dto);

    List<CategoriaDTO> listar();

    List<CategoriaDTO> filtrar(String tipoCategoria);

    CategoriaDTO obtenerPorId(Long id);

    CategoriaDTO actualizar(Long id, CategoriaDTO dto);

    void eliminar(Long id);
}
