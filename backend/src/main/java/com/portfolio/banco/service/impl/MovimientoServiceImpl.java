package com.portfolio.banco.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.portfolio.banco.dto.movimiento.MovimientoRequestDTO;
import com.portfolio.banco.dto.movimiento.MovimientoResponseDTO;
import com.portfolio.banco.entity.Cuenta;
import com.portfolio.banco.entity.Movimiento;
import com.portfolio.banco.enums.TipoMovimiento;
import com.portfolio.banco.exception.BusinessException;
import com.portfolio.banco.exception.ResourceNotFoundException;
import com.portfolio.banco.mapper.MovimientoMapper;
import com.portfolio.banco.repository.CuentaRepository;
import com.portfolio.banco.repository.MovimientoRepository;
import com.portfolio.banco.service.MovimientoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    @Value("${app.retiro.limite-diario}")
    private BigDecimal limiteDiario;

    @Override
    public MovimientoResponseDTO crear(MovimientoRequestDTO dto) {

        Cuenta cuenta = cuentaRepository.findById(dto.getCuentaId())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        BigDecimal saldoActual = obtenerSaldoActual(cuenta);

        BigDecimal valor = dto.getValor();

        // Ajustar signo según tipo
        if (dto.getTipoMovimiento() == TipoMovimiento.DEBITO) {
            valor = valor.abs().negate();
        } else {
            valor = valor.abs();
        }

        // Validar saldo
        BigDecimal nuevoSaldo = saldoActual.add(valor);

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Saldo no disponible");
        }

        // Validar cupo diario (solo débitos)
        if (dto.getTipoMovimiento() == TipoMovimiento.DEBITO) {

            LocalDate hoy = LocalDate.now();

            BigDecimal totalHoy = movimientoRepository.sumDebitosPorClienteEnRango(
                    cuenta.getCliente().getId(),
                    hoy.atStartOfDay(),
                    hoy.atTime(23, 59, 59)
            );

            BigDecimal nuevoTotal = totalHoy.add(valor.abs());

            if (nuevoTotal.compareTo(limiteDiario) > 0) {
                throw new BusinessException("Cupo diario Excedido");
            }
        }

        // Guardar movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento(dto.getTipoMovimiento());
        movimiento.setValor(valor);
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);

        return MovimientoMapper.toDTO(movimientoRepository.save(movimiento));
    }

    @Override
    public List<MovimientoResponseDTO> listar() {
        return movimientoRepository.findAll()
                .stream()
                .map(MovimientoMapper::toDTO)
                .toList();
    }

    @Override
    public List<MovimientoResponseDTO> listarPorCuenta(Long cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId)
                .stream()
                .map(MovimientoMapper::toDTO)
                .toList();
    }

    // Saldo actual
    private BigDecimal obtenerSaldoActual(Cuenta cuenta) {
        List<Movimiento> movimientos = movimientoRepository.findByCuentaId(cuenta.getId());

        if (movimientos.isEmpty()) {
            return cuenta.getSaldoInicial();
        }

        return movimientos.get(movimientos.size() - 1).getSaldo();
    }
}
