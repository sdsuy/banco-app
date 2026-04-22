package com.portfolio.banco.service;

import java.util.List;

import com.portfolio.banco.dto.movimiento.MovimientoRequestDTO;
import com.portfolio.banco.dto.movimiento.MovimientoResponseDTO;

public interface MovimientoService {

    MovimientoResponseDTO crear(MovimientoRequestDTO dto);

    List<MovimientoResponseDTO> listar();

    List<MovimientoResponseDTO> listarPorCuenta(Long cuentaId);
}
