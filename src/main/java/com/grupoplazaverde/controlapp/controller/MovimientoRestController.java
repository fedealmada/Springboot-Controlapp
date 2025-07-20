package com.grupoplazaverde.controlapp.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupoplazaverde.controlapp.Repository.MovimientoRepository;
import com.grupoplazaverde.controlapp.Repository.PersonaRepository;
import com.grupoplazaverde.controlapp.Repository.ProductoRepository;
import com.grupoplazaverde.controlapp.dtos.MovimientoDTO;
import com.grupoplazaverde.controlapp.enums.TipoMovimiento;
import com.grupoplazaverde.controlapp.mapper.MovimientoMapper;
import com.grupoplazaverde.controlapp.model.Movimiento;
import com.grupoplazaverde.controlapp.model.Persona;
import com.grupoplazaverde.controlapp.model.Producto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor

public class MovimientoRestController {

    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final PersonaRepository personaRepository;
    private final MovimientoMapper movimientoMapper;

public void eliminarPorId(Long id) {
    movimientoRepository.deleteById(id);
}


public void actualizar(Movimiento movimiento) {
    movimientoRepository.save(movimiento);
}


    @GetMapping
    public List<MovimientoDTO> listar() {
        return movimientoRepository.findAll(Sort.by(Sort.Direction.DESC, "fecha")).stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> obtenerPorId(@PathVariable Long id) {
        return movimientoRepository.findById(id)
                .map(m -> ResponseEntity.ok(movimientoMapper.toDTO(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody MovimientoDTO dto) {
        Optional<Producto> productoOpt = productoRepository.findById(dto.getProductoId());
        Optional<Persona> personaOpt = personaRepository.findById(dto.getPersonaId());

        if (productoOpt.isEmpty() || personaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Producto o Persona no encontrados");
        }

        Producto producto = productoOpt.get();
        Persona persona = personaOpt.get();

        TipoMovimiento tipo;
        try {
            tipo = TipoMovimiento.valueOf(dto.getTipo());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Tipo de movimiento inválido");
        }

        int cantidad = dto.getCantidad();
        if (cantidad <= 0) {
            return ResponseEntity.badRequest().body("La cantidad debe ser mayor a cero");
        }

        // Actualizar stock
        switch (tipo) {
            case ENTRADA:
            case DEVOLUCION:
                producto.setStock(producto.getStock() + cantidad);
                break;
            case SALIDA:
            case PRESTAMO:
                if (producto.getStock() < cantidad) {
                    return ResponseEntity.badRequest().body("Stock insuficiente para este movimiento");
                }
                producto.setStock(producto.getStock() - cantidad);
                break;
        }
        productoRepository.save(producto);

        Movimiento movimiento = new Movimiento();
        movimiento.setCantidad(cantidad);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipo(tipo);
        movimiento.setProducto(producto);
        movimiento.setPersona(persona);

        Movimiento guardado = movimientoRepository.save(movimiento);
        MovimientoDTO respuesta = movimientoMapper.toDTO(guardado);

        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!movimientoRepository.existsById(id))
            return ResponseEntity.notFound().build();
        movimientoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}