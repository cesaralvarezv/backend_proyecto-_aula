package com.backend_yireh_store.domain.port.out;

import com.backend_yireh_store.domain.model.LogEntry;
import java.util.List;

/**
 * Output Port — abstracción para persistir y consultar logs.
 * DIP: el dominio no depende de JPA.
 */
public interface LogRepositoryPort {
    LogEntry save(LogEntry log);
    List<LogEntry> findAll(int limit);
    List<LogEntry> findBySource(String source, int limit);
    List<LogEntry> findByLevel(String level, int limit);
}
