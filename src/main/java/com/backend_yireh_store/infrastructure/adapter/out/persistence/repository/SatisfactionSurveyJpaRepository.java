package com.backend_yireh_store.infrastructure.adapter.out.persistence.repository;

import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.SatisfactionSurveyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SatisfactionSurveyJpaRepository extends JpaRepository<SatisfactionSurveyEntity, Long> {

    Optional<SatisfactionSurveyEntity> findByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);
}
