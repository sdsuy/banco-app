package com.portfolio.banco.service;

import java.util.List;

import com.portfolio.banco.dto.cliente.ClienteRequestDTO;
import com.portfolio.banco.dto.cliente.ClienteResponseDTO;

public interface ClienteService {

    ClienteResponseDTO crear(ClienteRequestDTO dto);

    ClienteResponseDTO obtenerPorId(Long id);

    List<ClienteResponseDTO> listar();

    ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto);

    void eliminar(Long id);
}
