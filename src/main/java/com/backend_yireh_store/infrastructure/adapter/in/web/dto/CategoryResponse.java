package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import java.time.LocalDateTime;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        String icon,
        boolean active,
        int order,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
