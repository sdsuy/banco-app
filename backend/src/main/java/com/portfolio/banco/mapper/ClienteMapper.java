package com.portfolio.banco.mapper;

import com.portfolio.banco.dto.cliente.ClienteRequestDTO;
import com.portfolio.banco.dto.cliente.ClienteResponseDTO;
import com.portfolio.banco.entity.Cliente;

public class ClienteMapper {

    public static Cliente toEntity(ClienteRequestDTO dto) {
        Cliente c = new Cliente();

        c.setNombre(dto.getNombre());
        c.setGenero(dto.getGenero());
        c.setEdad(dto.getEdad());
        c.setIdentificacion(dto.getIdentificacion());
        c.setDireccion(dto.getDireccion());
        c.setTelefono(dto.getTelefono());

        c.setClienteId(dto.getClienteId());
        c.setContrasena(dto.getContrasena());
        c.setEstado(dto.getEstado());

        return c;
    }

    public static ClienteResponseDTO toDTO(Cliente c) {
        return ClienteResponseDTO.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .genero(c.getGenero())
                .edad(c.getEdad())
                .identificacion(c.getIdentificacion())
                .direccion(c.getDireccion())
                .telefono(c.getTelefono())
                .clienteId(c.getClienteId())
                .estado(c.getEstado())
                .build();
    }
}
