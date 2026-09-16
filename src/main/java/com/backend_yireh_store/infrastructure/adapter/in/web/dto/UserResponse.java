package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import com.backend_yireh_store.domain.model.User;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public record UserResponse(Long id, String name, String phone, String email,
                            Set<String> roles, BigDecimal balance) {
    public static UserResponse fromUser(User u) {
        Set<String> roleNames = u.getRoles() != null
                ? u.getRoles().stream().map(Enum::name).collect(Collectors.toSet())
                : Set.of();
        BigDecimal bal = u.getBalance() != null ? u.getBalance() : BigDecimal.ZERO;
        return new UserResponse(u.getId(), u.getName(), u.getPhone(), u.getEmail(), roleNames, bal);
    }
}

