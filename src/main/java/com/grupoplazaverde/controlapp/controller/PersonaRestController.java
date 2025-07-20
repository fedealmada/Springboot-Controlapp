package com.grupoplazaverde.controlapp.controller;

import java.util.List;
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

import com.grupoplazaverde.controlapp.Repository.PersonaRepository;
import com.grupoplazaverde.controlapp.Repository.ProductoRepository;
import com.grupoplazaverde.controlapp.dtos.PersonaDTO;
import com.grupoplazaverde.controlapp.dtos.ProductoDTO;
import com.grupoplazaverde.controlapp.mapper.PersonaMapper;
import com.grupoplazaverde.controlapp.model.Persona;

@RestController
@RequestMapping("/api/personas")
@CrossOrigin(origins = "*")
public class PersonaRestController {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PersonaMapper personaMapper;
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/buscar")
    public List<ProductoDTO> buscarPorNombre(@RequestParam String nombre) {
        return productoRepository.findByNombreStartingWithIgnoreCase(nombre)
                .stream()
                .map(producto -> ProductoDTO.builder()
                        .id(producto.getId())
                        .nombre(producto.getNombre())
                        .descripcion(producto.getDescripcion())
                        .stock(producto.getStock())
                        .ubicacion(producto.getUbicacion())
                        .tipo(producto.getTipo())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<PersonaDTO> listar() {
        return personaRepository.findAll().stream()
                .map(personaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTO> obtenerPorId(@PathVariable Long id) {
        return personaRepository.findById(id)
                .map(p -> ResponseEntity.ok(personaMapper.toDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PersonaDTO crear(@RequestBody PersonaDTO dto) {
        Persona persona = personaMapper.toEntity(dto);
        return personaMapper.toDTO(personaRepository.save(persona));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonaDTO> actualizar(@PathVariable Long id, @RequestBody PersonaDTO dto) {
        return personaRepository.findById(id)
                .map(persona -> {
                    persona.setNombre(dto.getNombre());
                    persona.setApellido(dto.getApellido());
                    persona.setEmpresa(dto.getEmpresa());
                    return ResponseEntity.ok(personaMapper.toDTO(personaRepository.save(persona)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!personaRepository.existsById(id))
            return ResponseEntity.notFound().build();
        personaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
