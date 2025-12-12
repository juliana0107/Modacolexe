package com.modacol.exe.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MensajeDTO {
    private Long id;
    
    @NotNull(message = "El destinatario es obligatorio")
    private Long destinatarioId;
    private String destinatarioNombre;
    
    private Long remitenteId;
    private String remitenteNombre;
    
    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 100, message = "El asunto no puede exceder 100 caracteres")
    private String asunto;
    
    @NotBlank(message = "El contenido es obligatorio")
    @Size(max = 1000, message = "El contenido no puede exceder 1000 caracteres")
    private String contenido;
    
    private LocalDateTime fechaEnvio;
    private Boolean leido = false;
}