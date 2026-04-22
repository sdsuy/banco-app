package com.portfolio.banco.mapper;

import com.portfolio.banco.dto.movimiento.MovimientoResponseDTO;
import com.portfolio.banco.entity.Movimiento;

public class MovimientoMapper {

    public static MovimientoResponseDTO toDTO(Movimiento m) {
        return MovimientoResponseDTO.builder()
                .id(m.getId())
                .fecha(m.getFecha())
                .tipoMovimiento(m.getTipoMovimiento())
                .valor(m.getValor())
                .saldo(m.getSaldo())
                .cuentaId(m.getCuenta().getId())
                .numeroCuenta(m.getCuenta().getNumeroCuenta())
                .build();
    }
}
