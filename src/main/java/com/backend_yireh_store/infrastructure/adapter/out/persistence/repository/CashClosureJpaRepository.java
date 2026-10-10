package com.backend_yireh_store.infrastructure.adapter.out.persistence.repository;

import com.backend_yireh_store.domain.model.CashClosureStatus;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.CashClosureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CashClosureJpaRepository extends JpaRepository<CashClosureEntity, Long> {

    Optional<CashClosureEntity> findFirstByStatusOrderByOpeningTimeDesc(CashClosureStatus status);

    Optional<CashClosureEntity> findFirstByCashierEmailAndStatusOrderByOpeningTimeDesc(String cashierEmail, CashClosureStatus status);
}
