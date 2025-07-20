package com.grupoplazaverde.controlapp.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.grupoplazaverde.controlapp.Repository.MovimientoRepository;
import com.grupoplazaverde.controlapp.Repository.PersonaRepository;
import com.grupoplazaverde.controlapp.Repository.ProductoRepository;
import com.grupoplazaverde.controlapp.enums.TipoMovimiento;
import com.grupoplazaverde.controlapp.model.Movimiento;
import com.grupoplazaverde.controlapp.model.Persona;
import com.grupoplazaverde.controlapp.model.Producto;

@Controller
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoRepository movimientoRepository;
    private final PersonaRepository personaRepository;
    private final ProductoRepository productoRepository;

    public MovimientoController(MovimientoRepository movimientoRepository,
                               PersonaRepository personaRepository,
                               ProductoRepository productoRepository) {
        this.movimientoRepository = movimientoRepository;
        this.personaRepository = personaRepository;
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public String listarMovimientos(Model model) {
        model.addAttribute("movimientos", movimientoRepository.findAll(Sort.by(Sort.Direction.DESC, "fecha")));
        model.addAttribute("personas", personaRepository.findAll());
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("tiposMovimiento", TipoMovimiento.values());
        return "movimientos";  // nombre de la plantilla Thymeleaf: movimientos.html
    }

    @PostMapping("/crear")
    public String crearMovimiento(@RequestParam Long personaId,
                                  @RequestParam Long productoId,
                                  @RequestParam int cantidad,
                                  @RequestParam String tipo,
                                  RedirectAttributes redirectAttrs) {
        try {
            Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
            Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            TipoMovimiento tipoMovimiento = TipoMovimiento.valueOf(tipo);

            // Actualizar stock según tipo
            switch (tipoMovimiento) {
                case ENTRADA:
                case DEVOLUCION:
                    producto.setStock(producto.getStock() + cantidad);
                    break;
                case SALIDA:
                case PRESTAMO:
                    if (producto.getStock() < cantidad) {
                        redirectAttrs.addFlashAttribute("error", "Stock insuficiente para este movimiento");
                        return "redirect:/movimientos";
                    }
                    producto.setStock(producto.getStock() - cantidad);
                    break;
            }

            productoRepository.save(producto);

            Movimiento movimiento = new Movimiento();
            movimiento.setCantidad(cantidad);
            movimiento.setFecha(LocalDateTime.now());
            movimiento.setTipo(tipoMovimiento);
            movimiento.setProducto(producto);
            movimiento.setPersona(persona);

            movimientoRepository.save(movimiento);

            redirectAttrs.addFlashAttribute("success", "Movimiento registrado correctamente");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/movimientos";
    }
}