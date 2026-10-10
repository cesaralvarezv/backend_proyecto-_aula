package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequestDto(
        String sessionId,
        @NotBlank(message = "El mensaje no puede estar vacío")
        String message
) {}
