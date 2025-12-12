package com.modacol.exe.controller;

import com.modacol.exe.dto.UsuarioDTO;
import com.modacol.exe.service.RolService;
import com.modacol.exe.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioViewController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    public UsuarioViewController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String nombre,
                        @RequestParam(required = false) String correo,
                        @RequestParam(required = false) Long rolId,
                        Model model) {
        List<UsuarioDTO> usuarios;
        if (nombre != null || correo != null || rolId != null) {
            usuarios = usuarioService.filtrar(nombre, correo, rolId);
        } else {
            usuarios = usuarioService.listar();
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("roles", rolService.listar());
        return "usuarios/list";
    }

    @GetMapping("/new")
    public String crear(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        model.addAttribute("roles", rolService.listar());
        return "usuarios/form";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.obtenerPorId(id));
        model.addAttribute("roles", rolService.listar());
        return "usuarios/form";
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute("usuario") UsuarioDTO form,
                          BindingResult result,
                          Model model) {

        // Validación extra: contraseña obligatoria solo al crear
        if (form.getId() == null &&
                (form.getPassword() == null || form.getPassword().isBlank())) {
            result.rejectValue("password", "NotBlank.usuario.password",
                    "La contraseña es obligatoria al crear un usuario");
        }

        // Si hay errores de validación, volver al formulario
        if (result.hasErrors()) {
            model.addAttribute("roles", rolService.listar());
            return "usuarios/form";
        }

        if (form.getId() == null) {
            // Crear
            usuarioService.crear(form);
        } else {
            // Editar
            UsuarioDTO usuario = usuarioService.obtenerPorId(form.getId());

            usuario.setNombre(form.getNombre());
            usuario.setCorreo(form.getCorreo());
            usuario.setRolId(form.getRolId());

            if (form.getPassword() != null && !form.getPassword().isBlank()) {
                usuario.setPassword(form.getPassword());
            }

            usuarioService.actualizar(usuario.getId(), usuario);
        }

        return "redirect:/usuarios";
    }

    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios";
    }
}
