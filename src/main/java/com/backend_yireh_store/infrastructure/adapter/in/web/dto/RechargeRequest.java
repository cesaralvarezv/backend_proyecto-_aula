package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;

public record RechargeRequest(
        BigDecimal amount,
        String paymentMethod,  // NEQUI, DAVIPLATA, CASH
        String referenceCode   // Opcional — número de comprobante
) {}
