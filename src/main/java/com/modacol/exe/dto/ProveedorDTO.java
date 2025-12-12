package com.modacol.exe.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProveedorDTO {
    private Long id;
    
    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 100, message = "La razón social no puede exceder 100 caracteres")
    private String razonSocial;

    @NotBlank(message = "La identificación es obligatoria")
    @Pattern(regexp = "^[0-9]+$", message = "La identificación solo puede contener números")
    private String identificacion;
    
    @Size(max = 150, message = "La dirección no puede exceder 150 caracteres")
    private String direccion;
    
    @Email(message = "El correo debe ser válido")
    @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
    private String correo;

    @Pattern(regexp = "^[0-9]{7,15}$", message = "El contacto debe tener entre 7 y 15 dígitos")
    private String contacto;
}
