package com.modacol.exe.impl;

import com.modacol.exe.dto.ProductoDTO;
import com.modacol.exe.entity.Producto;
import com.modacol.exe.entity.Proveedor;
import com.modacol.exe.repository.ProductoRepository;
import com.modacol.exe.repository.ProveedorRepository;
import com.modacol.exe.service.ProductoService;
import com.modacol.exe.specification.ProductoSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final ModelMapper modelMapper;

    public ProductoServiceImpl(ProductoRepository productoRepository,
                               ProveedorRepository proveedorRepository,
                               ModelMapper modelMapper) {
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional
    public ProductoDTO crear(ProductoDTO dto) {
        Producto producto = convertToEntity(dto);
        producto = productoRepository.save(producto);
        return convertToDto(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listar() {
        return productoRepository.findAllByOrderByIdAsc()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> filtrar(String nombre, String descripcion, Long proveedorId) {
        return productoRepository.findAll(ProductoSpecification.filtrar(nombre, descripcion, proveedorId))
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDTO buscarPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        return convertToDto(producto);
    }

    @Override
    @Transactional
    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));

        modelMapper.map(dto, producto);

        if (dto.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            producto.setProveedor(proveedor);
        } else {
            producto.setProveedor(null);
        }

        producto = productoRepository.save(producto);
        return convertToDto(producto);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado: " + id);
        }
        productoRepository.deleteById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarProveedores() {
        return proveedorRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, ProductoDTO.class))
                .toList();
    }


    private ProductoDTO convertToDto(Producto producto) {
        ProductoDTO dto = modelMapper.map(producto, ProductoDTO.class);

        if (producto.getProveedor() != null) {
            dto.setProveedorId(producto.getProveedor().getId());
            dto.setProveedorNombre(producto.getProveedor().getRazonSocial());
        }

        return dto;
    }

    private Producto convertToEntity(ProductoDTO dto) {
        Producto producto = modelMapper.map(dto, Producto.class);

        if (dto.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            producto.setProveedor(proveedor);
        }

        return producto;
    }
}
