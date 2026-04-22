package com.portfolio.banco.service.impl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.portfolio.banco.dto.reporte.ReporteDTO;
import com.portfolio.banco.entity.Movimiento;
import com.portfolio.banco.mapper.ReporteMapper;
import com.portfolio.banco.repository.MovimientoRepository;
import com.portfolio.banco.service.ReporteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final MovimientoRepository movimientoRepository;
    
    @Override
    public List<ReporteDTO> generar(Long clienteId, LocalDate inicio, LocalDate fin) {

        List<Movimiento> movimientos = movimientoRepository.findReporte(
                clienteId,
                inicio.atStartOfDay(),
                fin.atTime(23, 59, 59)
        );

        return movimientos.stream()
                .map(ReporteMapper::toDTO)
                .toList();
    }

    @Override
    public byte[] generarPdf(Long clienteId, LocalDate inicio, LocalDate fin) {

        List<ReporteDTO> data = generar(clienteId, inicio, fin);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document doc = new Document();
            PdfWriter.getInstance(doc, out);

            doc.open();

            doc.add(new Paragraph("Estado de Cuenta"));

            for (ReporteDTO r : data) {
                doc.add(new Paragraph(
                        r.getFecha() + " | " +
                        r.getCliente() + " | " +
                        r.getNumeroCuenta() + " | " +
                        r.getMovimiento() + " | " +
                        r.getSaldoDisponible()
                ));
            }

            doc.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF");
        }
    }

    @Override
    public String generarPdfBase64(Long clienteId, LocalDate inicio, LocalDate fin) {
        return Base64.getEncoder().encodeToString(generarPdf(clienteId, inicio, fin));
    }
}
