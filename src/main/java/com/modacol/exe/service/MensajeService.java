package com.modacol.exe.service;

import com.modacol.exe.dto.MensajeDTO;
import java.util.List;

public interface MensajeService {
    MensajeDTO enviar(MensajeDTO dto);
    List<MensajeDTO> obtenerRecibidos(Long userId);
    List<MensajeDTO> obtenerEnviados(Long userId);
    void marcarComoLeido(Long mensajeId);
    Long contarNoLeidos(Long userId);
}