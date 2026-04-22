package com.portfolio.banco.dto.movimiento;

import java.math.BigDecimal;

import com.portfolio.banco.enums.TipoMovimiento;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovimientoRequestDTO {

    @NotNull
    private TipoMovimiento tipoMovimiento;

    @NotNull
    private BigDecimal valor;

    @NotNull
    private Long cuentaId;
}
