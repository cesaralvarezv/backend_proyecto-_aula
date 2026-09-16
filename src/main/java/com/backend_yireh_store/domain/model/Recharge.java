package com.backend_yireh_store.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Dominio puro — representa un movimiento de recarga en la billetera del usuario. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recharge {
    private Long id;
    private String userId;          // email del usuario
    private BigDecimal amount;
    private String paymentMethod;   // NEQUI, DAVIPLATA, CASH
    private String status;          // PENDING, COMPLETED, REJECTED
    private String referenceCode;   // comprobante externo
    private LocalDateTime createdAt;
}
