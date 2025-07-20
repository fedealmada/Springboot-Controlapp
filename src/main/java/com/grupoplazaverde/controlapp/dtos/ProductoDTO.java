package com.grupoplazaverde.controlapp.dtos;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer stock;
    private String ubicacion;
    private String tipo;
    private Integer stockMinimo;
}