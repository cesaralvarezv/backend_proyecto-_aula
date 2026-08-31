package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import com.backend_yireh_store.domain.model.User;

import java.util.Set;

public record AuthResponse(Long id, String name, String phone, String email, Set<String> roles, String token) {
    public static AuthResponse fromUser(User u, String token) {
        Set<String> roleNames = u.getRoles() != null
                ? u.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet())
                : Set.of();
        return new AuthResponse(u.getId(), u.getName(), u.getPhone(), u.getEmail(), roleNames, token);
    }

    public static AuthResponse fromUser(User u) {
        return fromUser(u, null);
    }
}
