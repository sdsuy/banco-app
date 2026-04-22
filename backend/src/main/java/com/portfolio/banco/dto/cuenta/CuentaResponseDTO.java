package com.portfolio.banco.dto.cuenta;

import java.math.BigDecimal;

import com.portfolio.banco.enums.TipoCuenta;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CuentaResponseDTO {

    private Long id;
    private String numeroCuenta;
    private TipoCuenta tipoCuenta;
    private BigDecimal saldoInicial;
    private Boolean estado;

    private Long clienteId;
    private String nombreCliente;
}
