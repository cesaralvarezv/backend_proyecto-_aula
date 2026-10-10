package com.backend_yireh_store.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SatisfactionSurvey {

    private Long id;
    private Long orderId;
    private String userEmail;
    private int rating; // 1 a 5 estrellas
    private String quickTags; // Ej: "Temperatura perfecta, Rápida entrega"
    private String comment;
    private LocalDateTime createdAt;

    public static SatisfactionSurvey create(Long orderId, String userEmail, int rating, String quickTags, String comment) {
        if (orderId == null) throw new IllegalArgumentException("El ID de la orden es obligatorio.");
        if (userEmail == null || userEmail.isBlank()) throw new IllegalArgumentException("El email del cliente es obligatorio.");
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("La calificación debe estar entre 1 y 5 estrellas.");

        return SatisfactionSurvey.builder()
                .orderId(orderId)
                .userEmail(userEmail.trim().toLowerCase())
                .rating(rating)
                .quickTags(quickTags != null ? quickTags.trim() : "")
                .comment(comment != null ? comment.trim() : "")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
