package com.grupoplazaverde.controlapp.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.grupoplazaverde.controlapp.Repository.ProductoRepository;
import com.grupoplazaverde.controlapp.model.Producto;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/faltantes")
    public String mostrarFaltantes(Model model) {
        List<Producto> faltantes = productoRepository.findAll().stream()
                .filter(p -> p.getStock() != null && p.getStockMinimo() != null && p.getStock() < p.getStockMinimo())
                .toList();

        model.addAttribute("faltantes", faltantes);
        return "faltantes"; // Nombre del archivo Thymeleaf: faltantes.html
    }

    @GetMapping("/faltantes/pdf")
    public void exportarFaltantesPdf(HttpServletResponse response) throws IOException, DocumentException {
        List<Producto> faltantes = productoRepository.findAll().stream()
                .filter(p -> p.getStock() != null && p.getStockMinimo() != null && p.getStock() < p.getStockMinimo())
                .toList();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=faltantes.pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // --- Logo ---
        String logoPath = new ClassPathResource("static/img/logo.png").getFile().getAbsolutePath();
        Image logo = Image.getInstance(logoPath);
        logo.scaleAbsolute(100, 40);
        logo.setAlignment(Image.ALIGN_LEFT);
        document.add(logo);

        // --- Título principal ---
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
        Paragraph titulo = new Paragraph("Materiales Faltantes", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        // --- Fecha de generación ---
        Font fontFecha = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
        Paragraph fecha = new Paragraph(
                "Generado el: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), fontFecha);
        fecha.setAlignment(Element.ALIGN_RIGHT);
        fecha.setSpacingAfter(10);
        document.add(fecha);

        // --- Tabla ---
        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[] { 2.5f, 3f, 1.5f, 1.5f, 2.5f, 2.5f });
        tabla.setSpacingBefore(10);

        Font fontCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Stream.of("Nombre", "Descripción", "Stock", "Stock Mínimo", "Ubicación", "Tipo")
                .forEach(col -> {
                    PdfPCell header = new PdfPCell(new Phrase(col, fontCabecera));
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabla.addCell(header);
                });

        for (Producto p : faltantes) {
            tabla.addCell(p.getNombre());
            tabla.addCell(p.getDescripcion());
            tabla.addCell(String.valueOf(p.getStock()));
            tabla.addCell(String.valueOf(p.getStockMinimo()));
            tabla.addCell(p.getUbicacion());
            tabla.addCell(p.getTipo());
        }

        document.add(tabla);
        document.close();
    }

}