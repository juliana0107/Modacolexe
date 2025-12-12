package com.modacol.exe.controller;

import com.modacol.exe.dto.CategoriaDTO;
import com.modacol.exe.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categorias")
public class CategoriaViewController {

    private final CategoriaService categoriaService;

    public CategoriaViewController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String tipoCategoria,
                        Model model) {
        List<CategoriaDTO> categoriasList;
        if (tipoCategoria != null) {
            categoriasList = categoriaService.filtrar(tipoCategoria);
        } else {
            categoriasList = categoriaService.listar();
        }
        model.addAttribute("categoriasList", categoriasList);
        return "categoria/list";
    }

    @GetMapping("/new")
    public String crear(Model model) {
        CategoriaDTO categoria = new CategoriaDTO();
        model.addAttribute("categoria", categoria);
        return "categoria/form";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) {
        CategoriaDTO categoria = categoriaService.obtenerPorId(id);
        CategoriaDTO formDto = new CategoriaDTO();
        formDto.setId(categoria.getId());
        formDto.setTipoCategoria(categoria.getTipoCategoria());

        model.addAttribute("categoria", formDto);
        return "categoria/form";
    }

    @PostMapping
    public String guardar(@ModelAttribute("categoria") CategoriaDTO formDto) {

        if (formDto.getId() == null) {
            categoriaService.crear(formDto);
        } else {
            categoriaService.actualizar(formDto.getId(), formDto);
        }

        return "redirect:/categorias";
    }

    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return "redirect:/categorias";
    }
}
