package com.modacol.exe.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClienteDTO {
    private Long id;
    
    @NotBlank(message = "La empresa es obligatoria")
    @Size(max = 100, message = "La empresa no puede exceder 100 caracteres")
    private String empresa;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "La identificación es obligatoria")
    @Pattern(regexp = "^[0-9]+$", message = "La identificación solo puede contener números")
    @Size(max = 20, message = "La identificación no puede exceder 20 caracteres")
    private String identificacion;
    
    @Pattern(regexp = "^[0-9]{7,15}$", message = "El contacto debe tener entre 7 y 15 dígitos")
    private String contacto;
    
    @Email(message = "El correo debe ser válido")
    @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
    private String correo;
}
