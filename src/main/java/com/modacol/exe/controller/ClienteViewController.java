package com.modacol.exe.controller;

import com.modacol.exe.dto.ClienteDTO;
import com.modacol.exe.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteViewController {

    private final ClienteService clienteService;

    public ClienteViewController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String empresa,
                        @RequestParam(required = false) String nombre,
                        @RequestParam(required = false) String identificacion,
                        @RequestParam(required = false) String correo,
                        Model model) {
        List<ClienteDTO> clientes;
        if (empresa != null || nombre != null || identificacion != null || correo != null) {
            clientes = clienteService.filtrar(empresa, nombre, identificacion, correo);
        } else {
            clientes = clienteService.listar();
        }
        model.addAttribute("clientesList", clientes);
        return "clientes/list";
    }

    @GetMapping("/new")
    public String crear(Model model) {
        ClienteDTO cliente = new ClienteDTO();
        model.addAttribute("cliente", cliente);
        return "clientes/form";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) {
        ClienteDTO cliente = clienteService.obtenerPorId(id);
        model.addAttribute("cliente", cliente);
        return "clientes/form";
    }

    @PostMapping
    public String guardar(@ModelAttribute("cliente") ClienteDTO formDto) {



        if (formDto.getId() == null) {
            clienteService.crear(formDto);
        } else {
            clienteService.actualizar(formDto.getId(), formDto);
        }

        return "redirect:/clientes";
    }

    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return "redirect:/clientes";
    }
}
