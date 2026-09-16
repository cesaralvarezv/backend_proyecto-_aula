package com.backend_yireh_store.infrastructure.adapter.out.persistence;

import com.backend_yireh_store.domain.model.Recharge;
import com.backend_yireh_store.domain.port.out.RechargeRepositoryPort;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.repository.RechargeJpaRepository;
import com.backend_yireh_store.infrastructure.mapper.RechargeMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RechargePersistenceAdapter implements RechargeRepositoryPort {

    private final RechargeJpaRepository jpaRepository;
    private final RechargeMapper mapper;

    public RechargePersistenceAdapter(RechargeJpaRepository jpaRepository, RechargeMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Recharge save(Recharge recharge) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(recharge)));
    }

    @Override
    public List<Recharge> findByUserId(String userId) {
        return jpaRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(mapper::toDomain).toList();
    }
}
