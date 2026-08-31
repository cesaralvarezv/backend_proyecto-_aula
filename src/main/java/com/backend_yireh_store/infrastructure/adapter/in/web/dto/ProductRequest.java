package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Nombre es requerido")
        @Size(min = 1, max = 100, message = "Nombre maximo 100 caracteres")
        String name,

        @NotNull(message = "Precio es requerido")
        @DecimalMin(value = "0.01", message = "Precio debe ser mayor a cero")
        BigDecimal price,

        @Size(max = 500, message = "Descripcion maximo 500 caracteres")
        String description,

        @Size(max = 500, message = "URL de imagen maximo 500 caracteres")
        String imageUrl,

        @NotNull(message = "Categoria es requerida")
        Long categoryId,

        @Min(value = 0, message = "Orden no puede ser negativo")
        int order
) {}