package com.portfolio.banco.dto.movimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.portfolio.banco.enums.TipoMovimiento;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovimientoResponseDTO {

    private Long id;
    private LocalDateTime fecha;
    private TipoMovimiento tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;

    private Long cuentaId;
    private String numeroCuenta;
}
