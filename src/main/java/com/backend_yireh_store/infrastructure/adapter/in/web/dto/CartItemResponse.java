package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        Long productId,
        String productName,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal totalPrice
) {}