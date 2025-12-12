package com.modacol.exe.service;

import com.modacol.exe.dto.ProductoDTO;

import java.util.List;

public interface ProductoService {

    List<ProductoDTO> listar();

    List<ProductoDTO> filtrar(String nombre, String descripcion, Long proveedorId);

    List<ProductoDTO> listarProveedores();

    ProductoDTO buscarPorId(Long id);

    ProductoDTO crear(ProductoDTO dto);

    ProductoDTO actualizar(Long id, ProductoDTO dto);

    void eliminar(Long id);
}
