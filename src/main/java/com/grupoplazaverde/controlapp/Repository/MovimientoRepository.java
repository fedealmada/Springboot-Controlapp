package com.grupoplazaverde.controlapp.Repository;

import com.grupoplazaverde.controlapp.model.Movimiento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByPersonaId(Long personaId);
    List<Movimiento> findByProductoId(Long productoId);
    List<Movimiento> findByTipo(String tipo);
}
