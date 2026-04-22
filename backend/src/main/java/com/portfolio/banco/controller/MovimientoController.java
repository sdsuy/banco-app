package com.portfolio.banco.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.banco.dto.movimiento.MovimientoRequestDTO;
import com.portfolio.banco.dto.movimiento.MovimientoResponseDTO;
import com.portfolio.banco.service.MovimientoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService service;

    @PostMapping
    public MovimientoResponseDTO crear(@Valid @RequestBody MovimientoRequestDTO dto) {
        return service.crear(dto);
    }

    @GetMapping
    public List<MovimientoResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/cuenta/{cuentaId}")
    public List<MovimientoResponseDTO> listarPorCuenta(@PathVariable Long cuentaId) {
        return service.listarPorCuenta(cuentaId);
    }
}
