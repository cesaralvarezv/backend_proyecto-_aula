package com.backend_yireh_store.infrastructure.adapter.out.persistence;

import com.backend_yireh_store.domain.model.CashClosure;
import com.backend_yireh_store.domain.model.CashClosureStatus;
import com.backend_yireh_store.domain.port.out.CashClosureRepositoryPort;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.CashClosureEntity;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.repository.CashClosureJpaRepository;
import com.backend_yireh_store.infrastructure.mapper.CashClosureMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CashClosurePersistenceAdapter implements CashClosureRepositoryPort {

    private final CashClosureJpaRepository jpaRepository;
    private final CashClosureMapper mapper;

    public CashClosurePersistenceAdapter(CashClosureJpaRepository jpaRepository, CashClosureMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public CashClosure save(CashClosure cashClosure) {
        CashClosureEntity entity = mapper.toEntity(cashClosure);
        CashClosureEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<CashClosure> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<CashClosure> findFirstByStatus(CashClosureStatus status) {
        return jpaRepository.findFirstByStatusOrderByOpeningTimeDesc(status).map(mapper::toDomain);
    }

    @Override
    public Optional<CashClosure> findFirstByCashierEmailAndStatus(String cashierEmail, CashClosureStatus status) {
        return jpaRepository.findFirstByCashierEmailAndStatusOrderByOpeningTimeDesc(cashierEmail, status)
                .map(mapper::toDomain);
    }

    @Override
    public List<CashClosure> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
