package com.modacol.exe.controller;

import com.modacol.exe.service.MensajeService;
import com.modacol.exe.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class NotificacionController {

    @Autowired private MensajeService mensajeService;
    @Autowired private UsuarioService usuarioService;

    @ModelAttribute("mensajesNoLeidos")
    public Long mensajesNoLeidos(Authentication auth) {
        if (auth == null) return 0L;
        
        Long userId = usuarioService.listar().stream()
            .filter(u -> u.getCorreo().equals(auth.getName()))
            .findFirst()
            .map(u -> u.getId())
            .orElse(null);
            
        return userId != null ? mensajeService.contarNoLeidos(userId) : 0L;
    }
}