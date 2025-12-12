package com.modacol.exe.controller;

import com.modacol.exe.dto.CategoriaDTO;
import com.modacol.exe.dto.RolDTO;
import com.modacol.exe.service.RolService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/roles")
public class RolViewController {

    private final RolService rolService;

    public RolViewController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public String listar(Model model) {
        List<RolDTO> rolesList = rolService.listar();
        model.addAttribute("rolesList", rolService.listar());
        return "roles/list";
    }

    @GetMapping("/new")
    public String crear(Model model) {
        RolDTO rolDTO = new RolDTO();
        model.addAttribute("rol", rolDTO);
        return "roles/form";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) {
        RolDTO rolDTO = rolService.obtenerPorId(id);
        model.addAttribute("rol", rolDTO);
        return "roles/form";
    }


    @PostMapping
    public String guardar(@ModelAttribute("rol") RolDTO formDto) {

        if (formDto.getId() == null) {
            rolService.crear(formDto);
        } else {
            rolService.actualizar(formDto.getId(), formDto);
        }
        return "redirect:/roles";
    }

    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
        return "redirect:/roles";
    }
}
