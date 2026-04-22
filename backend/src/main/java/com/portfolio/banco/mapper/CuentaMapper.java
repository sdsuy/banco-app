package com.portfolio.banco.mapper;

import com.portfolio.banco.dto.cuenta.CuentaResponseDTO;
import com.portfolio.banco.entity.Cliente;
import com.portfolio.banco.entity.Cuenta;

public class CuentaMapper {

    public static Cuenta toEntity(Cliente cliente,
                                  String numeroCuenta,
                                  com.portfolio.banco.enums.TipoCuenta tipoCuenta,
                                  java.math.BigDecimal saldoInicial,
                                  Boolean estado) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(numeroCuenta);
        cuenta.setTipoCuenta(tipoCuenta);
        cuenta.setSaldoInicial(saldoInicial);
        cuenta.setEstado(estado);
        cuenta.setCliente(cliente);
        return cuenta;
    }

    public static CuentaResponseDTO toDTO(Cuenta cuenta) {
        return CuentaResponseDTO.builder()
                .id(cuenta.getId())
                .numeroCuenta(cuenta.getNumeroCuenta())
                .tipoCuenta(cuenta.getTipoCuenta())
                .saldoInicial(cuenta.getSaldoInicial())
                .estado(cuenta.getEstado())
                .clienteId(cuenta.getCliente().getId())
                .nombreCliente(cuenta.getCliente().getNombre())
                .build();
    }
}
