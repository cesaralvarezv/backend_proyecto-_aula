package com.backend_yireh_store.domain.model;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Domain model para logs centralizados.
 * Recopila errores de backend y frontend en un solo lugar.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LogEntry {

    @EqualsAndHashCode.Include
    private Long id;
    private String level;      // INFO, WARN, ERROR
    private String source;     // BACKEND, FRONTEND
    private String tag;        // AuthController, FlutterError, HttpInterceptor...
    private String message;
    private String stackTrace;
    private String platform;   // android, ios, web, windows
    private String deviceInfo;
    private LocalDateTime timestamp;

    // Factory para logs del backend
    public static LogEntry backend(String level, String tag, String message, String stackTrace) {
        return LogEntry.builder()
                .level(level)
                .source("BACKEND")
                .tag(tag)
                .message(message)
                .stackTrace(stackTrace)
                .platform("server")
                .deviceInfo("Spring Boot")
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Factory para logs del frontend
    public static LogEntry frontend(String level, String tag, String message,
                                    String stackTrace, String platform, String deviceInfo) {
        return LogEntry.builder()
                .level(level)
                .source("FRONTEND")
                .tag(tag)
                .message(message)
                .stackTrace(stackTrace)
                .platform(platform)
                .deviceInfo(deviceInfo)
                .timestamp(LocalDateTime.now())
                .build();
    }
}