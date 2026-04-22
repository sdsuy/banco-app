package com.portfolio.banco.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.banco.dto.cuenta.CuentaRequestDTO;
import com.portfolio.banco.dto.cuenta.CuentaResponseDTO;
import com.portfolio.banco.service.CuentaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService service;

    @PostMapping
    public CuentaResponseDTO crear(@Valid @RequestBody CuentaRequestDTO dto) {
        return service.crear(dto);
    }

    @GetMapping
    public List<CuentaResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public CuentaResponseDTO obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<CuentaResponseDTO> listarPorCliente(@PathVariable Long clienteId) {
        return service.listarPorCliente(clienteId);
    }

    @PutMapping("/{id}")
    public CuentaResponseDTO actualizar(@PathVariable Long id,
                                        @Valid @RequestBody CuentaRequestDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
