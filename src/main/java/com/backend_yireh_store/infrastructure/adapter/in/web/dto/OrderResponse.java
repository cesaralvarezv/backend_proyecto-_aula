package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        String userEmail,
        List<OrderItemResponse> items,
        BigDecimal total,
        String status,
        String paymentMethod,
        String deliveryLocation,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
