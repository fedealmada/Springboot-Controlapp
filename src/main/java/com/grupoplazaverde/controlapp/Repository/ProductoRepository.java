package com.grupoplazaverde.controlapp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupoplazaverde.controlapp.dtos.ProductoDTO;
import com.grupoplazaverde.controlapp.model.Producto;
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreStartingWithIgnoreCase(String nombre);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<ProductoDTO> findByNombre(String nombre);
}

