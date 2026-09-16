package com.backend_yireh_store.infrastructure.adapter.out.persistence.repository;

import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.RechargeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechargeJpaRepository extends JpaRepository<RechargeEntity, Long> {
    List<RechargeEntity> findByUserIdOrderByCreatedAtDesc(String userId);
}
