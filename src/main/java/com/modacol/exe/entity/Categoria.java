package com.modacol.exe.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "categorias")
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El tipo de categoría es obligatorio")
    @Size(min = 3, max = 100, message = "El tipo de categoría debe tener entre 3 y 100 caracteres")
    @Column(name = "tipo_categoria", length = 100)
    private String tipoCategoria;
}

