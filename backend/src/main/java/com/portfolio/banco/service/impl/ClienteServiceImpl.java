package com.portfolio.banco.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.portfolio.banco.dto.cliente.ClienteRequestDTO;
import com.portfolio.banco.dto.cliente.ClienteResponseDTO;
import com.portfolio.banco.entity.Cliente;
import com.portfolio.banco.exception.BusinessException;
import com.portfolio.banco.exception.ResourceNotFoundException;
import com.portfolio.banco.mapper.ClienteMapper;
import com.portfolio.banco.repository.ClienteRepository;
import com.portfolio.banco.service.ClienteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;
    
    @Override
    public ClienteResponseDTO crear(ClienteRequestDTO dto) {

        if (repository.existsByClienteId(dto.getClienteId())) {
            throw new BusinessException("El clienteId ya existe");
        }

        if (repository.existsByIdentificacion(dto.getIdentificacion())) {
            throw new BusinessException("La identificación ya existe");
        }

        Cliente cliente = ClienteMapper.toEntity(dto);

        return ClienteMapper.toDTO(repository.save(cliente));
    }

    @Override
    public ClienteResponseDTO obtenerPorId(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        return ClienteMapper.toDTO(cliente);
    }

    @Override
    public List<ClienteResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {

        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        cliente.setNombre(dto.getNombre());
        cliente.setGenero(dto.getGenero());
        cliente.setEdad(dto.getEdad());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEstado(dto.getEstado());

        return ClienteMapper.toDTO(repository.save(cliente));
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        repository.delete(cliente);
    }

}
