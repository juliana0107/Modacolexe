package com.modacol.exe.impl;

import com.modacol.exe.dto.ProveedorDTO;
import com.modacol.exe.entity.Proveedor;
import com.modacol.exe.repository.ProveedorRepository;
import com.modacol.exe.service.ProveedorService;
import com.modacol.exe.specification.ProveedorSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ModelMapper modelMapper;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository,
                                ModelMapper modelMapper) {
        this.proveedorRepository = proveedorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public ProveedorDTO crear(ProveedorDTO dto) {
        Proveedor proveedor = modelMapper.map(dto, Proveedor.class);
        proveedor = proveedorRepository.save(proveedor);
        return convertToDto(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDTO> listar() {
        return proveedorRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDTO> filtrar(String razonSocial, String identificacion, String correo) {
        return proveedorRepository.findAll(ProveedorSpecification.filtrar(razonSocial, identificacion, correo))
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDTO obtenerPorId(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        return convertToDto(proveedor);
    }

    @Override
    @Transactional
    public ProveedorDTO actualizar(Long id, ProveedorDTO dto) {

        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        modelMapper.map(dto, proveedor);

        proveedor = proveedorRepository.save(proveedor);

        return convertToDto(proveedor);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!proveedorRepository.existsById(id)) {
            throw new RuntimeException("Proveedor no encontrado");
        }
        proveedorRepository.deleteById(id);
    }

    private ProveedorDTO convertToDto(Proveedor proveedor) {
        return modelMapper.map(proveedor, ProveedorDTO.class);
    }
}
