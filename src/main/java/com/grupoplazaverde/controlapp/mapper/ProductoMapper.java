package com.grupoplazaverde.controlapp.mapper;

import org.springframework.stereotype.Component;

import com.grupoplazaverde.controlapp.dtos.ProductoDTO;
import com.grupoplazaverde.controlapp.model.Producto;

@Component
public class ProductoMapper {

    public ProductoDTO toDTO(Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .stock(producto.getStock())
                .ubicacion(producto.getUbicacion())
                .tipo(producto.getTipo())
                .stockMinimo(producto.getStockMinimo())  // <-- agregado
                .build();
    }

    public Producto toEntity(ProductoDTO dto) {
        return Producto.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .stock(dto.getStock())
                .ubicacion(dto.getUbicacion())
                .tipo(dto.getTipo())
                .stockMinimo(dto.getStockMinimo())  // <-- agregado
                .build();
    }
}