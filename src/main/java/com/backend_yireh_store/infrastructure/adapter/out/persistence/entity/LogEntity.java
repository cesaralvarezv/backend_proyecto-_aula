package com.backend_yireh_store.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 10)
    private String level;

    @Column(nullable = false, length = 20)
    private String source;

    @Column(nullable = false, length = 100)
    private String tag;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "LONGTEXT")
    private String stackTrace;

    @Column(length = 30)
    private String platform;

    @Column(length = 200)
    private String deviceInfo;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}