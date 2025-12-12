package com.modacol.exe.controller;

import com.modacol.exe.dto.UsuarioDTO;
import com.modacol.exe.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String mostrarPerfil(Model model, Authentication auth) {
        String correo = auth.getName();
        UsuarioDTO usuario = usuarioService.listar().stream()
            .filter(u -> u.getCorreo().equals(correo))
            .findFirst()
            .orElse(null);
            
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("esEdicion", true);
        }
        return "perfil/form";
    }

    @PostMapping
    public String actualizarPerfil(@Valid @ModelAttribute UsuarioDTO usuario, 
                                 BindingResult result, 
                                 Authentication auth,
                                 @RequestParam(required = false) String nuevaPassword,
                                 @RequestParam(required = false) String confirmarPassword,
                                 RedirectAttributes redirectAttributes) {
        
        String correoActual = auth.getName();
        UsuarioDTO usuarioActual = usuarioService.listar().stream()
            .filter(u -> u.getCorreo().equals(correoActual))
            .findFirst()
            .orElse(null);

        if (usuarioActual == null) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/perfil";
        }

        // Mantener el rol original
        usuario.setRolId(usuarioActual.getRolId());
        usuario.setId(usuarioActual.getId());

        // Validar cambio de contraseña
        if (nuevaPassword != null && !nuevaPassword.isEmpty()) {
            if (!nuevaPassword.equals(confirmarPassword)) {
                result.rejectValue("password", "error.password", "Las contraseñas no coinciden");
            } else if (nuevaPassword.length() < 8) {
                result.rejectValue("password", "error.password", "La contraseña debe tener al menos 8 caracteres");
            } else {
                usuario.setPassword(passwordEncoder.encode(nuevaPassword));
            }
        } else {
            // Mantener contraseña actual
            usuario.setPassword(usuarioActual.getPassword());
        }

        if (result.hasErrors()) {
            return "perfil/form";
        }

        try {
            usuarioService.actualizar(usuario.getId(), usuario);
            redirectAttributes.addFlashAttribute("success", "Perfil actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el perfil: " + e.getMessage());
        }

        return "redirect:/perfil";
    }
}