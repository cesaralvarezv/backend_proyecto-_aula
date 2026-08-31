package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank(message = "Nombre es requerido")
        @Size(min = 3, max = 120)
        String name,

        @NotBlank(message = "Celular es requerido")
        @Pattern(regexp = "^[0-9+\\s-]{7,20}$", message = "Celular inválido")
        String phone,

        @NotBlank(message = "Email es requerido")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Password es requerido")
        @Size(min = 6, max = 100, message = "Mínimo 6 caracteres")
        String password
) {}
