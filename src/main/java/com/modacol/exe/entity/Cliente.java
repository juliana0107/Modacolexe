package com.modacol.exe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "clientes")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotBlank(message = "La empresa es obligatoria")
    @Size(max = 100, message = "La empresa no puede exceder 100 caracteres")
    @Column(name = "empresa", nullable = false, length = 100)
    private String empresa;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La identificación es obligatoria")
    @Pattern(regexp = "^[0-9]+$", message = "La identificación solo puede contener números")
    @Size(max = 20, message = "La identificación no puede exceder 20 caracteres")
    @Column(name = "identificacion", nullable = false, unique = true, length = 20)
    private String identificacion;

    @Pattern(regexp = "^[0-9]{7,15}$", message = "El contacto debe tener entre 7 y 15 dígitos")
    @Column(name = "contacto", length = 20)
    private String contacto;

    @Email(message = "El correo debe ser válido")
    @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
    @Column(name = "correo", unique = true, length = 100)
    private String correo;
}
