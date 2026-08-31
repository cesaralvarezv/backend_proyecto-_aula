package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.*;

public record LogRequest(
        @NotBlank String level,
        @NotBlank String tag,
        @NotBlank String message,
        String stackTrace,
        String platform,
        String deviceInfo
) {}
