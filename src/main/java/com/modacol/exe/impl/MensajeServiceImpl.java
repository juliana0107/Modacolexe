package com.modacol.exe.impl;

import com.modacol.exe.dto.MensajeDTO;
import com.modacol.exe.entity.Mensaje;
import com.modacol.exe.repository.MensajeRepository;
import com.modacol.exe.repository.UsuarioRepository;
import com.modacol.exe.service.MensajeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MensajeServiceImpl implements MensajeService {

    @Autowired private MensajeRepository mensajeRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public MensajeDTO enviar(MensajeDTO dto) {
        Mensaje mensaje = modelMapper.map(dto, Mensaje.class);
        mensaje.setRemitente(usuarioRepository.findById(dto.getRemitenteId()).orElse(null));
        mensaje.setDestinatario(usuarioRepository.findById(dto.getDestinatarioId()).orElse(null));
        mensaje = mensajeRepository.save(mensaje);
        return convertirADTO(mensaje);
    }

    @Override
    public List<MensajeDTO> obtenerRecibidos(Long userId) {
        return mensajeRepository.findByDestinatarioIdOrderByFechaEnvioDesc(userId)
            .stream().map(this::convertirADTO).toList();
    }

    @Override
    public List<MensajeDTO> obtenerEnviados(Long userId) {
        return mensajeRepository.findByRemitenteIdOrderByFechaEnvioDesc(userId)
            .stream().map(this::convertirADTO).toList();
    }

    @Override
    public void marcarComoLeido(Long mensajeId) {
        mensajeRepository.findById(mensajeId).ifPresent(m -> {
            m.setLeido(true);
            mensajeRepository.save(m);
        });
    }

    @Override
    public Long contarNoLeidos(Long userId) {
        return mensajeRepository.countMensajesNoLeidos(userId);
    }

    private MensajeDTO convertirADTO(Mensaje mensaje) {
        MensajeDTO dto = modelMapper.map(mensaje, MensajeDTO.class);
        dto.setRemitenteNombre(mensaje.getRemitente().getNombre());
        dto.setDestinatarioNombre(mensaje.getDestinatario().getNombre());
        return dto;
    }
}