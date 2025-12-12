package com.modacol.exe.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class FlujoCajaDTO {

    private Long id;
    private LocalDate fechaFlujo;
    private String descripcion;
    private String tipo;
    private Double valorTotal;
    private Double saldoInicial;
    private Double saldoFinal;
    private List<DetalleFlujoCajaDTO> movimientos;
}
