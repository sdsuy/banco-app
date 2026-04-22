package com.portfolio.banco.service;

import java.time.LocalDate;
import java.util.List;

import com.portfolio.banco.dto.reporte.ReporteDTO;

public interface ReporteService {

    List<ReporteDTO> generar(Long clienteId, LocalDate inicio, LocalDate fin);

    byte[] generarPdf(Long clienteId, LocalDate inicio, LocalDate fin);

    String generarPdfBase64(Long clienteId, LocalDate inicio, LocalDate fin);
}
