package com.modacol.exe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "proveedores")
@Data
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 100, message = "La razón social no puede exceder 100 caracteres")
    @Column(name = "razon_social", length = 100)
    private String razonSocial;

    @NotNull(message = "La identificación es obligatoria")
    @Min(value = 1, message = "La identificación debe ser mayor a 0")
    @Column(name = "identificacion", unique = true, nullable = false)
    private Integer identificacion;

    @Size(max = 150, message = "La dirección no puede exceder 150 caracteres")
    @Column(name = "direccion", length = 150)
    private String direccion;

    @Email(message = "El correo debe ser válido")
    @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
    @Column(name = "correo", length = 100)
    private String correo;

    @Min(value = 1000000, message = "El contacto debe tener al menos 7 dígitos")
    @Max(value = 999999999999999L, message = "El contacto no puede exceder 15 dígitos")
    @Column(name = "contacto")
    private Long contacto;
}


