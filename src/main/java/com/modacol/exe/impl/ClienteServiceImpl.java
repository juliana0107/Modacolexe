package com.modacol.exe.impl;

import com.modacol.exe.dto.ClienteDTO;
import com.modacol.exe.entity.Cliente;
import com.modacol.exe.repository.ClienteRepository;
import com.modacol.exe.service.ClienteService;
import com.modacol.exe.specification.ClienteSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository,
                              ModelMapper modelMapper) {
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ClienteDTO crear(ClienteDTO dto) {
        Cliente entity = convertToEntity(dto);
        Cliente guardado = clienteRepository.save(entity);
        return convertToDto(guardado);
    }

    @Override
    public List<ClienteDTO> listar() {
        return clienteRepository.findAllByOrderByIdAsc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClienteDTO> filtrar(String empresa, String nombre, String identificacion, String correo) {
        return clienteRepository.findAll(ClienteSpecification.filtrar(empresa, nombre, identificacion, correo))
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
        return convertToDto(cliente);
    }

    @Override
    public ClienteDTO actualizar(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
        modelMapper.map(dto, cliente);

        Cliente actualizado = clienteRepository.save(cliente);
        return convertToDto(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }


    private ClienteDTO convertToDto(Cliente cliente) {
        return modelMapper.map(cliente, ClienteDTO.class);
    }

    private Cliente convertToEntity(ClienteDTO dto) {
        return modelMapper.map(dto, Cliente.class);
    }
}
