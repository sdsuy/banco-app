package com.portfolio.banco.service;

import java.util.List;

import com.portfolio.banco.dto.cuenta.CuentaRequestDTO;
import com.portfolio.banco.dto.cuenta.CuentaResponseDTO;

public interface CuentaService {

    CuentaResponseDTO crear(CuentaRequestDTO dto);

    CuentaResponseDTO obtenerPorId(Long id);

    List<CuentaResponseDTO> listar();

    List<CuentaResponseDTO> listarPorCliente(Long clienteId);

    CuentaResponseDTO actualizar(Long id, CuentaRequestDTO dto);

    void eliminar(Long id);
}
