package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long id,
        String userId,
        List<CartItemResponse> items,
        int itemCount,
        BigDecimal total
) {}