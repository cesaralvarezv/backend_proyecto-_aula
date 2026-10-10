package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import java.time.LocalDateTime;

public record ChatResponseDto(
        String sessionId,
        String role,
        String content,
        LocalDateTime timestamp
) {}
