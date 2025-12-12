package com.modacol.exe.controller;

import com.modacol.exe.dto.CompraDTO;
import com.modacol.exe.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<CompraDTO> crear(@Valid @RequestBody CompraDTO dto) {
        // Aquí ya se validan:
        // - proveedorId, usuarioId obligatorios
        // - fechaCompra, fechaEstimadaEntrega
        // - detalles notEmpty, etc.
        CompraDTO creada = compraService.crear(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getId())
                .toUri();

        return ResponseEntity.created(location).body(creada);
    }

    @GetMapping
    public ResponseEntity<List<CompraDTO>> listar() {
        return ResponseEntity.ok(compraService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDTO> obtener(@PathVariable Long id) {
        CompraDTO dto = compraService.obtenerPorId(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraDTO> actualizar(@PathVariable Long id,
                                                @Valid @RequestBody CompraDTO dto) {
        // Por seguridad, “forzamos” que el id del path gane al del body
        dto.setId(id);
        CompraDTO actualizado = compraService.actualizar(id, dto);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        compraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
