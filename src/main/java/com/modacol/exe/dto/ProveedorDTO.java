package com.modacol.exe.dto;

import lombok.Data;

@Data
public class ProveedorDTO {
    private Long id;
    private String razonSocial;
    private String identificacion;
    private String direccion;
    private String correo;
    private String contacto;
}
