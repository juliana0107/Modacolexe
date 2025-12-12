package com.modacol.exe.impl;

import com.modacol.exe.dto.RolDTO;
import com.modacol.exe.entity.Rol;
import com.modacol.exe.repository.RolRepository;
import com.modacol.exe.service.RolService;
import com.modacol.exe.specification.RolSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private final ModelMapper modelMapper;

    public RolServiceImpl(RolRepository rolRepository, ModelMapper modelMapper) {
        this.rolRepository = rolRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public RolDTO crear(RolDTO dto) {
        Rol rol = modelMapper.map(dto, Rol.class);
        rol = rolRepository.save(rol);
        return convertToDto(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> listar() {
        return rolRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> filtrar(String tipo) {
        return rolRepository.findAll(RolSpecification.filtrar(tipo))
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RolDTO obtenerPorId(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        return convertToDto(rol);
    }

    @Override
    @Transactional
    public RolDTO actualizar(Long id, RolDTO dto) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        modelMapper.map(dto, rol);

        rol = rolRepository.save(rol);

        return convertToDto(rol);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado");
        }
        rolRepository.deleteById(id);
    }

    private RolDTO convertToDto(Rol rol) {
        return modelMapper.map(rol, RolDTO.class);
    }
}
