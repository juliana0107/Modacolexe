package com.modacol.exe.impl;

import com.modacol.exe.dto.CategoriaDTO;
import com.modacol.exe.entity.Categoria;
import com.modacol.exe.repository.CategoriaRepository;
import com.modacol.exe.service.CategoriaService;
import com.modacol.exe.specification.CategoriaSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository,
                                ModelMapper modelMapper) {
        this.categoriaRepository = categoriaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoriaDTO crear(CategoriaDTO dto) {
        Categoria categoria = convertToEntity(dto);
        Categoria guardada = categoriaRepository.save(categoria);
        return convertToDto(guardada);
    }

    @Override
    public List<CategoriaDTO> listar() {
        return categoriaRepository.findAllByOrderByIdAsc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoriaDTO> filtrar(String tipoCategoria) {
        return categoriaRepository.findAll(CategoriaSpecification.filtrar(tipoCategoria))
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoriaDTO obtenerPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
        return convertToDto(categoria);
    }

    @Override
    public CategoriaDTO actualizar(Long id, CategoriaDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));

        modelMapper.map(dto, categoria);

        Categoria actualizada = categoriaRepository.save(categoria);

        return convertToDto(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }

    private CategoriaDTO convertToDto(Categoria categoria) {
        return modelMapper.map(categoria, CategoriaDTO.class);
    }

    private Categoria convertToEntity(CategoriaDTO dto) {
        return modelMapper.map(dto, Categoria.class);
    }
}
