package com.modacol.exe.controller;

import com.modacol.exe.entity.Usuario;
import com.modacol.exe.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class RoleThemeController {

    private final UsuarioRepository usuarioRepository;

    @ModelAttribute
    public void addRoleTheme(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(auth -> auth.startsWith("ROLE_"))
                    .findFirst()
                    .orElse("ROLE_EMPLEADO");
            
            String theme = switch (role) {
                case "ROLE_ADMIN" -> "admin";
                case "ROLE_FLUJO_DE_CAJA" -> "flujo";
                default -> "operativo";
            };
            
            String username = authentication.getName();
            Usuario usuario = usuarioRepository.findByCorreo(username).orElse(null);
            String nombreUsuario = usuario != null ? usuario.getNombre() : "Usuario";
            
            model.addAttribute("userRole", role.replace("ROLE_", ""));
            model.addAttribute("userTheme", theme);
            model.addAttribute("userName", nombreUsuario);
        }
    }
}