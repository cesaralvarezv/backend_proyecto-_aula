package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.model.LogEntry;
import com.backend_yireh_store.domain.port.in.LogUseCase;
import com.backend_yireh_store.domain.port.out.LogRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LogService implements LogUseCase {

    private final LogRepositoryPort logRepository;

    public LogService(LogRepositoryPort logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    @Transactional
    public LogEntry register(LogEntry log) {
        return logRepository.save(log);
    }

    @Override
    @Transactional
    public LogEntry registerBackend(String level, String tag, String message, String stackTrace) {
        LogEntry entry = LogEntry.backend(level, tag, message, stackTrace);
        return logRepository.save(entry);
    }

    @Override
    @Transactional
    public LogEntry registerFrontend(String level, String tag, String message, String stackTrace,
                                     String platform, String deviceInfo) {
        LogEntry entry = LogEntry.frontend(level, tag, message, stackTrace, platform, deviceInfo);
        return logRepository.save(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogEntry> getAll(int limit) {
        return logRepository.findAll(Math.min(limit, 500));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogEntry> getBySource(String source, int limit) {
        return logRepository.findBySource(source, Math.min(limit, 500));
    }
}
