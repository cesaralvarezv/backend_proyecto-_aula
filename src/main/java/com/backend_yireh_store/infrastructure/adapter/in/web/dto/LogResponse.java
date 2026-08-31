package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import java.time.LocalDateTime;

public record LogResponse(Long id, String level, String source, String tag, String message,
                          String platform, String deviceInfo, LocalDateTime timestamp) {}
