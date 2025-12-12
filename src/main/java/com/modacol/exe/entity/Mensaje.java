package com.modacol.exe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
@Data
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "remitente_id")
    private Usuario remitente;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    private Usuario destinatario;

    @NotBlank
    @Size(max = 100)
    @Column(name = "asunto")
    private String asunto;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "contenido")
    private String contenido;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "leido")
    private Boolean leido = false;

    @PrePersist
    public void prePersist() {
        fechaEnvio = LocalDateTime.now();
    }
}