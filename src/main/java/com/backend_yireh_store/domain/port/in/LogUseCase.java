package com.backend_yireh_store.domain.port.in;

import com.backend_yireh_store.domain.model.LogEntry;
import java.util.List;

/**
 * Input Port — caso de uso para gestionar logs.
 */
public interface LogUseCase {
    LogEntry register(LogEntry log);
    LogEntry registerBackend(String level, String tag, String message, String stackTrace);
    LogEntry registerFrontend(String level, String tag, String message, String stackTrace,
                              String platform, String deviceInfo);
    List<LogEntry> getAll(int limit);
    List<LogEntry> getBySource(String source, int limit);
}
