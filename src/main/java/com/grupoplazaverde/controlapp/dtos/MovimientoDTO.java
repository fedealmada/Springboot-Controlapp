package com.grupoplazaverde.controlapp.dtos;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class MovimientoDTO {
    private Long id;
    private Long productoId;
    private Long personaId;
    private Integer cantidad;
    private String tipo; 
    private LocalDateTime fecha;
}
