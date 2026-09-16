package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RechargeResponse(
        Long id,
        String userId,
        BigDecimal amount,
        String paymentMethod,
        String status,
        String referenceCode,
        LocalDateTime createdAt
) {}
