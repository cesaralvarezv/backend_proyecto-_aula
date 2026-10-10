package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloseCashClosureRequest {

    @NotNull(message = "El efectivo físico declarado es requerido para cerrar caja")
    @DecimalMin(value = "0.0", message = "El efectivo declarado no puede ser negativo")
    private BigDecimal declaredCash;

    private String notes;
}
