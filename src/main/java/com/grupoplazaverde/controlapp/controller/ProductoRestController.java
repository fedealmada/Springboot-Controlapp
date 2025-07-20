package com.grupoplazaverde.controlapp.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupoplazaverde.controlapp.Repository.ProductoRepository;
import com.grupoplazaverde.controlapp.dtos.ProductoDTO;
import com.grupoplazaverde.controlapp.mapper.ProductoMapper;
import com.grupoplazaverde.controlapp.model.Producto;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoRestController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMapper productoMapper;

    @GetMapping("/buscar")
    public List<ProductoDTO> buscarPorNombre(@RequestParam String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(producto -> ProductoDTO.builder()
                        .id(producto.getId())
                        .nombre(producto.getNombre())
                        .descripcion(producto.getDescripcion())
                        .stock(producto.getStock())
                        .ubicacion(producto.getUbicacion())
                        .tipo(producto.getTipo())
                        .stockMinimo(producto.getStockMinimo())
                        .build())
                .collect(Collectors.toList());
    }

    
@GetMapping("/faltantes")
public List<ProductoDTO> listarFaltantes() {
    return productoRepository.findAll().stream()
        .filter(p -> p.getStock() != null && p.getStockMinimo() != null && p.getStock() < p.getStockMinimo())
        .map(productoMapper::toDTO)
        .collect(Collectors.toList());
}

    @GetMapping
    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(producto -> ResponseEntity.ok(productoMapper.toDTO(producto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProductoDTO crear(@RequestBody ProductoDTO dto) {
        Producto producto = productoMapper.toEntity(dto);
        Producto guardado = productoRepository.save(producto);
        return productoMapper.toDTO(guardado);
    }

    @PutMapping("/actualizar/stock/{id}")
    public ResponseEntity<Producto> actualizarStock(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (!productoOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Producto producto = productoOpt.get();

        // Solo actualizás lo que te interesa
        if (body.containsKey("stock")) {
            Number nuevoStock = (Number) body.get("stock");
            producto.setStock(nuevoStock.intValue());
        }

        productoRepository.save(producto);
        return ResponseEntity.ok(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(dto.getNombre());
                    producto.setDescripcion(dto.getDescripcion());
                    producto.setStock(dto.getStock());
                    producto.setUbicacion(dto.getUbicacion());
                    producto.setTipo(dto.getTipo());
                    producto.setStockMinimo(dto.getStockMinimo());
                    productoRepository.save(producto);
                    return ResponseEntity.ok(productoMapper.toDTO(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
@PutMapping("/actualizar-por-nombre")
public ResponseEntity<String> actualizarPorNombre(@RequestBody ProductoDTO dto) {
    Producto producto = (Producto) productoRepository.findByNombre(dto.getNombre());
    if (producto == null) {
        return ResponseEntity.notFound().build();
    }

    if (dto.getStock() != null) {
        producto.setStock(dto.getStock());
    }
    if (dto.getStockMinimo() != null) {
        producto.setStockMinimo(dto.getStockMinimo());
    }

    productoRepository.save(producto);
    return ResponseEntity.ok("Actualizado correctamente");
}

}