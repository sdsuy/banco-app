package com.portfolio.banco.mapper;

import com.portfolio.banco.dto.reporte.ReporteDTO;
import com.portfolio.banco.entity.Movimiento;

public class ReporteMapper {

    public static ReporteDTO toDTO(Movimiento m) {

        return ReporteDTO.builder()
                .fecha(m.getFecha())
                .cliente(m.getCuenta().getCliente().getNombre())
                .numeroCuenta(m.getCuenta().getNumeroCuenta())
                .tipoCuenta(m.getCuenta().getTipoCuenta().name())
                .saldoInicial(m.getCuenta().getSaldoInicial())
                .estado(m.getCuenta().getEstado())
                .movimiento(m.getValor())
                .saldoDisponible(m.getSaldo())
                .build();
    }
}
