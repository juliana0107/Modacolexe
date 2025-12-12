package com.modacol.exe.controller;

import com.modacol.exe.dto.MensajeDTO;
import com.modacol.exe.service.MensajeService;
import com.modacol.exe.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/mensajes")
public class MensajeController {

    @Autowired private MensajeService mensajeService;
    @Autowired private UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model, Authentication auth) {
        Long userId = obtenerUserId(auth);
        model.addAttribute("recibidos", mensajeService.obtenerRecibidos(userId));
        model.addAttribute("enviados", mensajeService.obtenerEnviados(userId));
        model.addAttribute("usuarios", usuarioService.listar());
        model.addAttribute("mensaje", new MensajeDTO());
        return "mensajes/index";
    }

    @PostMapping("/enviar")
    public String enviar(@Valid @ModelAttribute MensajeDTO mensaje, 
                        BindingResult result, 
                        Authentication auth,
                        RedirectAttributes redirectAttributes) {
        
        mensaje.setRemitenteId(obtenerUserId(auth));
        
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Error en el formulario");
            return "redirect:/mensajes";
        }

        try {
            mensajeService.enviar(mensaje);
            redirectAttributes.addFlashAttribute("success", "Mensaje enviado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al enviar mensaje");
        }

        return "redirect:/mensajes";
    }

    @PostMapping("/marcar-leido/{id}")
    public String marcarLeido(@PathVariable Long id) {
        mensajeService.marcarComoLeido(id);
        return "redirect:/mensajes";
    }

    private Long obtenerUserId(Authentication auth) {
        return usuarioService.listar().stream()
            .filter(u -> u.getCorreo().equals(auth.getName()))
            .findFirst()
            .map(u -> u.getId())
            .orElse(null);
    }
}