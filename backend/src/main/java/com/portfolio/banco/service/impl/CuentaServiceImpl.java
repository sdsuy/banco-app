package com.portfolio.banco.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.portfolio.banco.dto.cuenta.CuentaRequestDTO;
import com.portfolio.banco.dto.cuenta.CuentaResponseDTO;
import com.portfolio.banco.entity.Cliente;
import com.portfolio.banco.entity.Cuenta;
import com.portfolio.banco.exception.BusinessException;
import com.portfolio.banco.exception.ResourceNotFoundException;
import com.portfolio.banco.mapper.CuentaMapper;
import com.portfolio.banco.repository.ClienteRepository;
import com.portfolio.banco.repository.CuentaRepository;
import com.portfolio.banco.service.CuentaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public CuentaResponseDTO crear(CuentaRequestDTO dto) {
        if (cuentaRepository.existsByNumeroCuenta(dto.getNumeroCuenta())) {
            throw new BusinessException("El número de cuenta ya existe");
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        Cuenta cuenta = CuentaMapper.toEntity(
                cliente,
                dto.getNumeroCuenta(),
                dto.getTipoCuenta(),
                dto.getSaldoInicial(),
                dto.getEstado()
        );

        return CuentaMapper.toDTO(cuentaRepository.save(cuenta));
    }

    @Override
    public CuentaResponseDTO obtenerPorId(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        return CuentaMapper.toDTO(cuenta);
    }

    @Override
    public List<CuentaResponseDTO> listar() {
        return cuentaRepository.findAll()
                .stream()
                .map(CuentaMapper::toDTO)
                .toList();
    }

    @Override
    public List<CuentaResponseDTO> listarPorCliente(Long clienteId) {
        return cuentaRepository.findByClienteId(clienteId)
                .stream()
                .map(CuentaMapper::toDTO)
                .toList();
    }

    @Override
    public CuentaResponseDTO actualizar(Long id, CuentaRequestDTO dto) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        if (!cuenta.getNumeroCuenta().equals(dto.getNumeroCuenta())
                && cuentaRepository.existsByNumeroCuenta(dto.getNumeroCuenta())) {
            throw new BusinessException("El número de cuenta ya existe");
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setSaldoInicial(dto.getSaldoInicial());
        cuenta.setEstado(dto.getEstado());
        cuenta.setCliente(cliente);

        return CuentaMapper.toDTO(cuentaRepository.save(cuenta));
    }

    @Override
    public void eliminar(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        cuentaRepository.delete(cuenta);
    }

}
