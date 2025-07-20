package com.grupoplazaverde.controlapp.model;

import java.time.LocalDateTime;

import com.grupoplazaverde.controlapp.enums.TipoMovimiento;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movimientos")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Persona persona;

    @ManyToOne
    private Producto producto;

    private Integer cantidad;

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo;

    private LocalDateTime fecha;
}