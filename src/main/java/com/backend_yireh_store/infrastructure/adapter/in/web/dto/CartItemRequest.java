package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequest(
        @NotNull(message = "Product ID es requerido")
        Long productId,

        @Min(value = 1, message = "Cantidad mínima es 1")
        int quantity
) {}