package com.grupoplazaverde.controlapp.mapper;

import org.springframework.stereotype.Component;
import com.grupoplazaverde.controlapp.dtos.PersonaDTO;
import com.grupoplazaverde.controlapp.model.Persona;

@Component
public class PersonaMapper {

    public PersonaDTO toDTO(Persona persona) {
        if (persona == null) return null;

        return PersonaDTO.builder()
                .id(persona.getId())
                .nombre(persona.getNombre())
                .apellido(persona.getApellido())
                .empresa(persona.getEmpresa())
                .build();
    }

    public Persona toEntity(PersonaDTO dto) {
        if (dto == null) return null;

        return Persona.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .empresa(dto.getEmpresa())
                .build();
    }
}