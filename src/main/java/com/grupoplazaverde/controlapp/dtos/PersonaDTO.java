package com.grupoplazaverde.controlapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PersonaDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String empresa;
}
