package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.*;

public record CategoryRequest(
        @NotBlank(message = "Nombre es requerido")
        @Size(min = 1, max = 100, message = "Nombre maximo 100 caracteres")
        String name,

        @Size(max = 500, message = "Descripcion maximo 500 caracteres")
        String description,

        @NotBlank(message = "Icono es requerido")
        String icon,

        @NotBlank(message = "Ubicación es requerida")
        @Size(min = 1, max = 100, message = "Ubicación maximo 100 caracteres")
        String location,

        @Min(value = 0, message = "Orden no puede ser negativo")
        int order
) {}
