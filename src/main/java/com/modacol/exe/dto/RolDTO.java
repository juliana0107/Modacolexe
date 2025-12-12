package com.modacol.exe.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RolDTO {

    private Long id;

    @NotBlank(message = "El tipo de rol es obligatorio")
    @Size(max = 50, message = "El tipo de rol no puede exceder 50 caracteres")
    @Pattern(
            regexp = "^[A-ZÁÉÍÓÚÑ\\s]+$",
            message = "El rol solo puede contener letras y debe estar en MAYÚSCULAS"
    )
    private String tipo;

    public void setTipo(String tipo) {
        this.tipo = (tipo != null) ? tipo.toUpperCase() : null;
    }
}

