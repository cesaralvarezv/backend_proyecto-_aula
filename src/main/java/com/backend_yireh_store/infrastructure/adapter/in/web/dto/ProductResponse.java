package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        String description,
        String imageUrl,
        Long categoryId,
        String categoryName,
        boolean active,
        int order,
        int quantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}