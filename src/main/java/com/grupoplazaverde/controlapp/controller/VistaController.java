package com.grupoplazaverde.controlapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.grupoplazaverde.controlapp.Repository.ProductoRepository;
import com.grupoplazaverde.controlapp.dtos.ProductoDTO;
import com.grupoplazaverde.controlapp.mapper.ProductoMapper;

@Controller

public class VistaController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMapper productoMapper;

    @GetMapping("/inventario")
    public String mostrarInventario(Model model) {
        List<ProductoDTO> lista = productoRepository.findAll()
                .stream()
                .map(productoMapper::toDTO)
                .toList();

        model.addAttribute("productos", lista); // "productos" es el nombre con el que lo accedés en la vista
        model.addAttribute("producto", new ProductoDTO());
        return "inventario"; // Thymeleaf buscará templates/inventario.html
    }

    @GetMapping("/personas")
    public String mostrarPersonas() {
        return "personas"; // Thymeleaf buscará templates/personas.html
    }

    @GetMapping("/panel")
    public String mostrarPanel() {
        return "panel"; // Thymeleaf buscará templates/personas.html
    }
}
