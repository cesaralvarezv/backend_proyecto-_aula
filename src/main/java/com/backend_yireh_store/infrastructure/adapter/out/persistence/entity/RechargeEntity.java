package com.backend_yireh_store.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recharges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RechargeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "reference_code", nullable = false, length = 100)
    private String referenceCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
