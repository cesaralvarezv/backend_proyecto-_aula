package com.backend_yireh_store.infrastructure.adapter.out.persistence;

import com.backend_yireh_store.domain.model.LogEntry;
import com.backend_yireh_store.domain.port.out.LogRepositoryPort;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.LogEntity;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.repository.SpringLogJpaRepository;
import com.backend_yireh_store.infrastructure.mapper.LogMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogPersistenceAdapter implements LogRepositoryPort {

    private final SpringLogJpaRepository jpaRepository;
    private final LogMapper mapper;

    public LogPersistenceAdapter(SpringLogJpaRepository jpaRepository,
                                 LogMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public LogEntry save(LogEntry log) {
        LogEntity entity = mapper.toEntity(log);
        LogEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<LogEntry> findAll(int limit) {
        return jpaRepository.findTopNByOrderByTimestampDesc(limit)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<LogEntry> findBySource(String source, int limit) {
        return jpaRepository.findTopNBySourceOrderByTimestampDesc(source, limit)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<LogEntry> findByLevel(String level, int limit) {
        return jpaRepository.findTopNByLevelOrderByTimestampDesc(level, limit)
                .stream().map(mapper::toDomain).toList();
    }
}