package com.backend_yireh_store.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

/**
 * Domain model — sin dependencias de Spring/JPA (SRP, DIP).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "passwordHash")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String passwordHash;
    @Builder.Default
    private Set<RoleName> roles = EnumSet.of(RoleName.USER);
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
    private LocalDateTime createdAt;

    public static User createNew(String name, String phone, String email, String passwordHash) {
        if (name == null || name.trim().length() < 3) throw new IllegalArgumentException("Nombre minimo 3 caracteres");
        if (phone == null || phone.trim().length() < 7) throw new IllegalArgumentException("Celular invalido");
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) throw new IllegalArgumentException("Correo invalido");
        if (passwordHash == null || passwordHash.isBlank()) throw new IllegalArgumentException("Password requerido");
        return User.builder()
                .name(name.trim())
                .phone(phone.trim())
                .email(email.trim().toLowerCase())
                .passwordHash(passwordHash)
                .roles(EnumSet.of(RoleName.USER))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public boolean hasRole(RoleName role) {
        return roles != null && roles.contains(role);
    }

    public boolean isAdmin() {
        return hasRole(RoleName.ADMIN);
    }
}