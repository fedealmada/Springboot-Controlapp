package com.grupoplazaverde.controlapp.mapper;


import org.springframework.stereotype.Component;

import com.grupoplazaverde.controlapp.dtos.MovimientoDTO;
import com.grupoplazaverde.controlapp.enums.TipoMovimiento;
import com.grupoplazaverde.controlapp.model.Movimiento;
import com.grupoplazaverde.controlapp.model.Persona;
import com.grupoplazaverde.controlapp.model.Producto;

@Component
public class MovimientoMapper {

    public MovimientoDTO toDTO(Movimiento movimiento) {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(movimiento.getId());
        dto.setCantidad(movimiento.getCantidad());
        dto.setFecha(movimiento.getFecha());
        dto.setTipo(movimiento.getTipo().name()); // ← cambio acá
        dto.setProductoId(movimiento.getProducto().getId());
        dto.setPersonaId(movimiento.getPersona().getId());
        return dto;
    }

    public Movimiento toEntity(MovimientoDTO dto, Producto producto, Persona persona) {
        Movimiento movimiento = new Movimiento();
        movimiento.setId(dto.getId());
        movimiento.setCantidad(dto.getCantidad());
        movimiento.setFecha(dto.getFecha());
        movimiento.setTipo(TipoMovimiento.valueOf(dto.getTipo())); // convertir el string a enum
        movimiento.setProducto(producto);
        movimiento.setPersona(persona);
        return movimiento;
    }
}