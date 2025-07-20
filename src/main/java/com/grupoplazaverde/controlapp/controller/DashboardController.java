package com.grupoplazaverde.controlapp.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.grupoplazaverde.controlapp.Repository.MovimientoRepository;
import com.grupoplazaverde.controlapp.Repository.ProductoRepository;
import com.grupoplazaverde.controlapp.model.Movimiento;
import com.grupoplazaverde.controlapp.model.Producto;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ProductoRepository productoRepository;
    private final MovimientoRepository movimientoRepository;

    @GetMapping
    public String mostrarDashboard(Model model) {
        List<Producto> productos = productoRepository.findAll();
        List<Producto> stockBajo = productos.stream()
                .filter(p -> p.getStock() != null && p.getStockMinimo() != null)
                .filter(p -> p.getStock() <= p.getStockMinimo())
                .collect(Collectors.toList());

        List<Movimiento> movimientosRecientes = movimientoRepository
                .findAll(Sort.by(Sort.Direction.DESC, "fecha"))
                .stream()
                .limit(5)
                .toList();

        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("stockTotal", productos.stream()
                .mapToInt(p -> p.getStock() != null ? p.getStock() : 0).sum());
        model.addAttribute("stockBajo", stockBajo);
        model.addAttribute("movimientosRecientes", movimientosRecientes);

        return "dashboard"; // templates/dashboard.html
    }
}