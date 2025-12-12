package com.modacol.exe.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(
            regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ\\s]+$",
            message = "El nombre solo puede contener letras y espacios"
    )
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser válido")
    @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
    private String correo;

    @Size(min = 8, max = 200, message = "La contraseña debe tener entre 8 y 200 caracteres")
    private String password;

    private String rolTipo;

    @NotNull(message = "El rol es obligatorio")
    private Long rolId;
}
