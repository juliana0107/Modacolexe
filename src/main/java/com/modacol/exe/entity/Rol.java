package com.modacol.exe.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data

public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotBlank(message = "El tipo de rol es obligatorio")
    @Size(max = 50, message = "El tipo de rol no puede exceder 50 caracteres")
    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;
}