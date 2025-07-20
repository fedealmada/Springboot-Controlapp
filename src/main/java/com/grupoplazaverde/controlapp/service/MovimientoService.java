package com.grupoplazaverde.controlapp.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.grupoplazaverde.controlapp.Repository.MovimientoRepository;
import com.grupoplazaverde.controlapp.Repository.PersonaRepository;
import com.grupoplazaverde.controlapp.Repository.ProductoRepository;
import com.grupoplazaverde.controlapp.enums.TipoMovimiento;
import com.grupoplazaverde.controlapp.model.Movimiento;
import com.grupoplazaverde.controlapp.model.Persona;
import com.grupoplazaverde.controlapp.model.Producto;

import jakarta.transaction.Transactional;

@Service
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final PersonaRepository personaRepository;

    public MovimientoService(MovimientoRepository movimientoRepository,
                            ProductoRepository productoRepository,
                            PersonaRepository personaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
        this.personaRepository = personaRepository;
    }

    @Transactional
    public Movimiento crearMovimiento(Long personaId, Long productoId, int cantidad, TipoMovimiento tipo) {
        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        switch (tipo) {
            case ENTRADA:
            case DEVOLUCION:
                producto.setStock(producto.getStock() + cantidad);
                break;
            case SALIDA:
            case PRESTAMO:
                if (producto.getStock() < cantidad) {
                    throw new RuntimeException("Stock insuficiente");
                }
                producto.setStock(producto.getStock() - cantidad);
                break;
            default:
                throw new RuntimeException("Tipo de movimiento inválido");
        }

        productoRepository.save(producto);

        Movimiento movimiento = new Movimiento();
        movimiento.setPersona(persona);
        movimiento.setProducto(producto);
        movimiento.setCantidad(cantidad);
        movimiento.setTipo(tipo);
        movimiento.setFecha(LocalDateTime.now());

        return movimientoRepository.save(movimiento);
    }

    public List<Movimiento> listarMovimientos() {
        return movimientoRepository.findAll(Sort.by(Sort.Direction.DESC, "fecha"));
    }
}